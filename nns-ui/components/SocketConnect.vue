<template>
    <div />
</template>

<script>
import SockJS from "sockjs-client"
import Stomp from "webstomp-client"

export default {
    name: 'SocketConnect',

    data: () => ({
        socketInfo: {},

        keepAliveId: 0
    }),

    created() {
        const userId = this.$newWebId()
        const status = 'DISCONNECTED'

        this.socketInfo = {
            userId,
            status
        }

        // broadcast the event
        this.$root.$emit('onSocketInfoUpdated', this.socketInfo)

        this.socketConnect()
    },

    methods: {
        socketConnect() {
            const url = `${this.$config.socketUrl}`
            console.log(`Connecting Web Socket to ${url}`)

            const socket = new SockJS(url)
            const options = { debug: false, heartbeat: false, protocols: ['v12.stomp'] }
            this.stompClient = Stomp.over(socket, options)

            this.stompClient.connect(
                {},
                frame => {
                    console.log("Web Socket connected.")

                    const { command } = frame
                    this.socketInfo = { ...this.socketInfo, status: command }
                    this.$root.$emit('onSocketInfoUpdated', this.socketInfo)

                    this.stompClient.subscribe("/topic/ws", message => {
                        const { body } = message
                        const messageData = JSON.parse(body)
                        console.log(messageData)
                        this.$root.$emit('onSocketMessage', messageData)
                    })
                },
                error => {
                    console.log(error)
                }
            )

            socket.onclose = () => {
                console.log("Web Socket closed.")

                this.socketInfo = { ...this.socketInfo, status: 'DISCONNECTED' }
                this.$root.$emit('onSocketInfoUpdated', this.socketInfo)
            }
        }
    }
}
</script>
