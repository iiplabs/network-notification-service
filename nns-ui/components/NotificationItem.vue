<template>
    <div class="NotificationsItem">
        {{ webId }} / {{ sourcePhone }} / {{ destinationPhone }}
    </div>
</template>

<script>
export default {
    name: 'NotificationsItem',

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
    }
}
</script>
