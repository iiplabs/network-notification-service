<template>
    <div class="NotificationRequestsView">
        <div class="d-flex flex-row justify-space-between align-baseline">
            <SmallTitle v-if="title">{{ title }}</SmallTitle>
            <v-btn :small="$vuetify.breakpoint.xsOnly" color="secondary" outlined :disabled="showProgress"
                :loading="showProgress" @click="clear">
                {{ $t('notification_requests_view.clear') }}
            </v-btn>
        </div>

        <div class="d-flex flex-row">
            <v-checkbox v-model="hideCompleted" :label="$t('notification_requests_view.hide_completed')" color="error"
                hide-details class="mx-auto"></v-checkbox>
        </div>

        <div class="d-flex flex-row justify-space-between my-8">
            <div>Total - {{ total }}</div>
            <div>Posted - {{ posted }}</div>
            <div>Delayed - {{ delayed }}</div>
            <div>Completed - {{ completed }}</div>
        </div>

        <div v-if="completed < posted" class="d-flex flex-column my-8">
            <div>Completion:</div>
            <v-progress-linear height="45" color="blue-grey">
                {{ Math.ceil(progressCompletion) }}%
            </v-progress-linear>
        </div>

        <div class="d-flex flex-column">
            <v-data-iterator :items="requests" :items-per-page="-1">
                <template #default="props">
                    <div class="d-flex flex-wrap">
                        <NotificationRequestItem v-for="item in filterRequestsByCompletedStatus(props.items)"
                            :key="item.webId" :item="item" />
                    </div>
                </template>
            </v-data-iterator>
        </div>

        <v-divider class="mt-8 mt-md-4" />
    </div>
</template>

<script>
export default {
    name: 'NotificationRequestsView',

    props: {
        title: {
            type: String,
            required: false,
            default: null
        },
        showProgress: {
            type: Boolean,
            required: false,
            default: false
        }
    },

    data: () => ({
        requests: [],

        unmatched: [],

        hideCompleted: true
    }),

    computed: {
        completed() {
            return this.requests.filter(r => {
                const { messages } = r
                return messages.findIndex(m => m.status === 'COMPLETED') > -1
            }).length
        },

        delayed() {
            return this.requests.filter(r => {
                const { messages } = r
                return messages.findIndex(m => m.status === 'SMS_DELAYED') > -1
            }).length
        },

        posted() {
            return this.requests.filter(r => {
                const { status } = r
                return status === 'OK'
            }).length
        },

        total() {
            return this.requests.length
        },

        progressCompletion() {
            return (this.completed / this.posted) * 100;
        }
    },

    created() {
        this.$root.$on('onAddNotificationRequest', (payload) => {
            this.requests.push({ ...payload, messages: [] })
        })

        this.$root.$on('onSocketMessage', (payload) => {
            if (!this.addSocketMessageToRequest(payload)) {
                const { webId } = payload
                if (webId) {
                    console.log(`Problem matching request for webId ${webId}`)
                    this.unmatched.push({ ...payload, processed: false })
                } else {
                    console.log('Message has no webId; to be discarded', payload)
                }
            }
        })
    },

    mounted() {
        this.processUnmatchedMessages();
    },

    methods: {
        clear() {
            this.requests = []
        },

        addSocketMessageToRequest(socketMessage) {
            let status = true

            const { webId } = socketMessage
            if (webId) {
                const webIdRequests = this.requests.filter(i => i.webId === webId)
                if (webIdRequests.length > 0) {
                    const r = webIdRequests[0]
                    const { messages } = r
                    messages.push({ ...socketMessage, localId: this.$newWebId() })
                } else {
                    status = false
                }
            }

            return status
        },

        processUnmatchedMessages() {
            for (const message of this.unmatched) {
                if (this.addSocketMessageToRequest(message)) {
                    Object.assign(message, { ...message, processed: true });
                }
            }

            this.unmatched = this.unmatched.filter(i => !i.processed)

            setTimeout(() => {
                this.processUnmatchedMessages();
            }, 10000);
        },

        filterRequestsByCompletedStatus(collection) {
            if (this.hideCompleted) {
                return [...collection].filter(r => {
                    const { messages } = r
                    return messages.findIndex(m => m.status === 'COMPLETED') < 0
                })
            } else {
                return [...collection]
            }
        }
    }
}
</script>

<style scoped>
.NotificationRequestsView {
    margin-top: 1rem;
}
</style>
