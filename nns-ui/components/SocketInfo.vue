<template>
    <div class="d-flex flex-row align-center">
        <v-icon v-if="connected" color="green" dense class="mx-2">mdi-antenna</v-icon>
        <v-icon v-else color="red" dense class="mx-2">mdi-antenna</v-icon>
        <div>id: {{ userId }}</div>
    </div>
</template>

<script>
export default {
    name: 'SocketInfo',

    props: {
        info: {
            type: Object,
            required: true,
        }
    },

    computed: {
        userId() {
            const { userId } = this.info
            return userId
        },

        connected() {
            const { status } = this.info
            return status && status === 'CONNECTED'
        }
    }
}
</script>