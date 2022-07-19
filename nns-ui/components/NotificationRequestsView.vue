<template>
    <div class="NotificationRequestsView">
        <SmallTitle v-if="title">{{ title }} ({{ total }})</SmallTitle>

        <div class="d-flex flex-column">
            <v-data-iterator :items="requests" :items-per-page="-1">
                <template #default="props">
                    <div class="d-flex flex-wrap">
                        <NotificationRequestItem v-for="item in props.items" :key="item.webId" :item="item" />
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
        }
    },

    created() {
        this.$root.$on('onClearNotificationRequests', () => {
            this.requests = []
        })

        this.$root.$on('onAddNotificationRequest', (payload) => {
            this.requests.push(payload)
        })
    },

    data: () => ({
        requests: []
    }),

    computed: {
        total() {
            return this.requests.length
        }
    }
}
</script>

<style scoped>
.NotificationRequestsView {
    margin-top: 1rem;
}
</style>
