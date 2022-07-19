<template>
    <div class="NotificationRequestItem ma-2">
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
                <div class="mx-2">{{ status }}</div>
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
            const { status } = this.item
            return status
        },
    }
}
</script>
