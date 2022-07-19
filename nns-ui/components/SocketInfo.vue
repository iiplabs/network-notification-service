<template>
    <div>
        <v-icon v-if="connected" v-bind="size" color="green">mdi-antenna</v-icon>
        <v-icon v-else v-bind="size" color="red">mdi-antenna</v-icon>
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
        connected() {
            const { status } = this.info
            return status && status === 'CONNECTED'
        },

        size() {
            const size = { xs: 'x-small', sm: 'small', md: 'dense', lg: 'large', xl: 'x-large' }[
                this.$vuetify.breakpoint.name
            ]
            return size ? { [size]: true } : {}
        }
    }
}
</script>