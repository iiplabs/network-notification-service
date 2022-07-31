<template>
    <div class="NotificationRequestItem pa-4">
        <div class="d-flex flex-column">
            <div class="d-flex flex-row">
                <div>{{ $t('notification_requests_view.item.webId') }}</div>
                <div class="mx-2">{{ webId }}</div>
            </div>
            <div class="d-flex flex-row">
                <div>{{ $t('notification_requests_view.item.sourcePhone') }}</div>
                <div class="mx-2">{{ msisdnB }}</div>
            </div>
            <div class="d-flex flex-row">
                <div>{{ $t('notification_requests_view.item.destinationPhone') }}</div>
                <div class="mx-2">{{ msisdnA }}</div>
            </div>
            <div class="d-flex flex-row">
                <div>{{ $t('notification_requests_view.item.status') }}</div>
                <div class="mx-2">
                    <span v-if="statusOK" class="green--text text--darken-1">
                        {{ status }}
                    </span>
                    <span v-else class="red--text text--darken-3">
                        {{ status }}
                    </span>
                </div>
            </div>

            <div class="d-flex flex-column">
                <div class="my-2">
                    <span>{{ $t('notification_requests_view.item.messages.title') }}</span>
                    <span v-if="completed" class="green--text text--darken-1">OK</span>
                </div>
                <div v-for="(message, index) in sortedMessagesByTimeStamp(item.messages)" :key="message.localId">
                    <div>
                        <span>{{ index + 1 }}.</span>
                        <span>{{ message.timeStamp }}</span>
                        <span> - </span>
                        <span>{{ message.status }}</span>
                    </div>
                    <div v-if="message.comments">
                        <span>{{ $t('notification_requests_view.item.messages.item.comments') }}</span>
                        <span> </span>
                        <span>{{ message.comments }}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'NotificationRequestItem',

    props: {
        item: {
            type: Object,
            required: true,
        },
    },

    computed: {
        webId() {
            const { webId } = this.item
            return webId
        },
        msisdnB() {
            const { msisdnB } = this.item
            return msisdnB
        },
        msisdnA() {
            const { msisdnA } = this.item
            return msisdnA
        },
        status() {
            const { status, statusCode } = this.item
            return `${status} (${statusCode})`
        },
        statusOK() {
            return this.status.startsWith('OK')
        },

        completed() {
            const { messages } = this.item
            return messages.findIndex(m => m.status === 'COMPLETED') > -1
        }
    },

    methods: {
        sortedMessagesByTimeStamp(collection) {
            return [...collection].sort(function (a, b) {
                return (new Date(a.timeStamp)).getTime() - (new Date(b.timeStamp)).getTime()
            })
        }
    }
}
</script>
