<template>
    <div class="NotificationsItem">
        {{ webId }} / {{ msisdnB }} / {{ msisdnA }}
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
        msisdnB() {
            const { msisdnB } = this.item
            return msisdnB
        },
        msisdnA() {
            const { msisdnA } = this.item
            return msisdnA
        },
    }
}
</script>
