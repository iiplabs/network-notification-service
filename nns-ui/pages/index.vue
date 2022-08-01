<template>
    <v-container fluid fill-height class="mb-4 pa-1">
        <v-card flat height="100%" width="100%">
            <v-card-text>
                <TestInstructions class="mb-4" />

                <TestSetupControl :title="$t('tests_setup.title')" :min="0" :max="2000" :show-progress="progress"
                    :socket-info="socketInfo" @controlUpdated="generateTestNotifications" />

                <div v-if="progress" class="d-flex flex-column my-8">
                    <div>Posting:</div>
                    <v-progress-linear :active="progress" height="45" color="blue-grey">
                        {{ Math.ceil(progressPost) }}%
                    </v-progress-linear>
                </div>

                <Requests429View v-if="requests429.length > 0" :title="$t('requests429.title')" :items="requests429" />
                <NotificationRequestsView :title="$t('notification_requests_view.title')" :show-progress="progress" />
            </v-card-text>
        </v-card>

        <SocketConnect :log-web-socket="false" />
    </v-container>
</template>

<script>
export default {
    name: "IndexPage",
    data: () => ({
        socketInfo: null,
        requests429: [],

        currentRequestsCounter: 0,
        totalRequestsToPost: 0,
    }),

    head() {
        return {
            title: `${this.$t("title")}`
        };
    },

    computed: {
        progress() {
            return this.currentRequestsCounter > 0
        },

        progressPost() {
            return ((this.totalRequestsToPost - this.currentRequestsCounter) / this.totalRequestsToPost) * 100;
        }
    },

    created() {
        this.$root.$on("onSocketInfoUpdated", (payload) => {
            console.log("Received socket info:", payload);
            this.socketInfo = { ...payload };
        });

        this.$on("onPostRequest", async (payload) => {
            const response = await this.sendRequest(payload).catch((error) => {
                console.log(error);
                setTimeout(() => {
                    this.$emit('onPostRequest', payload);
                }, 1000);
            });

            if (response) {
                const { webId, status, statusCode } = response;
                if (statusCode === 429) {
                    console.log('429 - Too many requests');
                    const status = 'FAIL';
                    this.requests429.push({ ...payload, webId, status, statusCode });
                } else if ([500, 501, 502].includes(statusCode)) {
                    console.log('Internal server error for request, repeating in 1 sec', payload);
                    setTimeout(() => {
                        this.$emit('onPostRequest', payload);
                    }, 1000);
                    return;
                } else {
                    this.$root.$emit("onAddNotificationRequest", { ...payload, webId, status, statusCode });
                }
                this.$nextTick(() => {
                    if (this.currentRequestsCounter > 0) {
                        this.currentRequestsCounter--;
                    }
                });
            } else {
                setTimeout(() => {
                    this.$emit('onPostRequest', payload);
                }, 1000);
            }
        });
    },

    methods: {
        generateTestNotifications(o) {
            const notificationRequests = [];
            console.log("Generating tests with parameters: ", o);
            const { testsNumber, strategy, delay } = o;
            for (let ii = 0; ii < testsNumber; ii++) {
                const webId = this.$newWebId();
                const msisdnA = this.getRandomPhone();
                const msisdnB = this.getRandomPhone();
                const n = {
                    webId,
                    msisdnA,
                    msisdnB
                };
                notificationRequests.push(n);
            }
            if (strategy === "SIM") {
                this.sendSimultaneously(notificationRequests, testsNumber);
            }
            else if (strategy === "SEQ") {
                this.sendSequentially(notificationRequests, delay, testsNumber);
            }
        },
        getRandomPhone() {
            const webId = this.$newWebId();
            return webId.substring(webId.length - 11, webId.length)
        },
        async sendSequentially(requests, delay, testsNumber) {
            this.currentRequestsCounter = testsNumber;
            this.totalRequestsToPost = testsNumber;
            for (const r of requests) {
                const response = await this.sendRequest(r).catch((error) => {
                    console.log(error);
                });
                const { webId, status, statusCode } = response
                if (statusCode === 429) {
                    console.log('429 - Too many requests');
                    const status = 'FAIL';
                    this.requests429.push({ ...r, webId, status, statusCode });
                } else if ([500, 501, 502].includes(statusCode)) {
                    console.log('Internal server error for request, repeating in 1 sec', r);
                    setTimeout(() => {
                        this.$emit('onPostRequest', r);
                    }, 1000);
                } else {
                    this.$root.$emit("onAddNotificationRequest", { ...r, webId, status, statusCode });
                }
                await this.$sleep(delay);
                this.currentRequestsCounter--;
            }
        },
        sendSimultaneously(requests, testsNumber) {
            this.currentRequestsCounter = testsNumber;
            this.totalRequestsToPost = testsNumber;
            // transform into bunch of promises
            requests.forEach(r => {
                this.$emit('onPostRequest', r);
            });
        },
        async sendRequest(r) {
            let { webId } = r;
            let status = "OK";
            let statusCode = 200;

            const { msisdnA, msisdnB } = r;
            const data = await this.$axios
                .$post("/unavailableSubscriber", { msisdnA, msisdnB })
                .catch(e => {
                    status = "FAIL";
                    if (e && e.response) {
                        statusCode = e.response.status;
                    } else {
                        statusCode = 500;
                    }
                });
            if (data) {
                webId = data.webId;
            }
            
            return { webId, status, statusCode };
        }
    },
}
</script>
