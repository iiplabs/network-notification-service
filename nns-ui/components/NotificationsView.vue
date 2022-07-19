<template>
    <div class="NotificationsView">
        <div class="d-flex flex-row justify-space-between">
            <SmallTitle v-if="title">{{ title }} ({{ total }})</SmallTitle>
            <v-btn :small="$vuetify.breakpoint.xsOnly" color="secondary" outlined :disabled="showProgress"
                :loading="showProgress" @click="clearNotifications">
                {{ $t('notifications_view.clear') }}
                <template #loader>
                    <span class="cmac-loader">
                        <v-icon light>mdi-cached</v-icon>
                    </span>
                </template>
            </v-btn>
        </div>

        <div class="d-flex flex-column">
            <v-data-iterator :items="notifications" :items-per-page="-1">
                <template #default="props">
                    <div class="d-flex flex-column pa-2">
                        <NotificationItem v-for="item in props.items" :key="item.webId" :item="item" />
                    </div>
                </template>
            </v-data-iterator>
        </div>

        <v-divider class="mt-8 mt-md-4" />
    </div>
</template>

<script>
export default {
    name: 'NotificationsView',

    props: {
        title: {
            type: String,
            required: false,
            default: null
        },
        notifications: {
            type: Array,
            required: true
        },
        showProgress: {
            type: Boolean,
            required: false,
            default: false
        }
    },

    data: () => ({
        //
    }),

    computed: {
        total() {
            return this.notifications.length
        }
    },

    methods: {
        clearNotifications() {
            this.$emit('clearNotifications')
        }
    }
}
</script>

<style scoped>
.NotificationsView {
    margin-top: 1rem;
}
</style>
