<template>
    <div class="NotificationRequestItem ma-2">
        <div class="d-flex flex-column">
            <div class="d-flex flex-row">
                <div>Web Id:</div>
                <div class="mx-2">{{ webId }}</div>
            </div>
            <div class="d-flex flex-row">
                <div>Source Phone:</div>
                <div class="mx-2">{{ sourcePhone }}</div>
            </div>
            <div class="d-flex flex-row">
                <div>Destination Phone:</div>
                <div class="mx-2">{{ destinationPhone }}</div>
            </div>
            <div class="d-flex flex-row">
                <div>POST status:</div>
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
        sourcePhone() {
            const { sourcePhone } = this.item
            return sourcePhone
        },
        destinationPhone() {
            const { destinationPhone } = this.item
            return destinationPhone
        },
        status() {
            const { status } = this.item
            return status
        },
    }
}
</script>
