<template>
  <v-container fluid fill-height class="mb-4 pa-1">
    <v-card flat height="100%" width="100%">
      <v-card-text>
        <TestInstructions class="mb-4" />

        <TestSetupControl :title="$t('tests_setup.title')" :min="0" :max="2000" :show-progress="progress"
          :socket-info="socketInfo" @controlUpdated="generateTestNotifications" />

        <NotificationRequestsView :title="$t('notification_requests_view.title')" />
      </v-card-text>
    </v-card>

    <SocketConnect />
  </v-container>
</template>

<script>
export default {
  name: "IndexPage",

  data: () => ({
    progress: false,
    socketInfo: null
  }),

  head() {
    return {
      title: `${this.$t('title')}`
    }
  },

  created() {
    this.$root.$on('onSocketInfoUpdated', (payload) => {
      console.log('Received socket info:', payload)
      this.socketInfo = { ...payload }
    })
  },

  methods: {
    generateTestNotifications(o) {
      const notificationRequests = []
      console.log('Generating tests with parameters: ', o);
      const { testsNumber, strategy, delay } = o;
      for (let ii = 0; ii < testsNumber; ii++) {
        const webId = this.$newWebId()
        const msisdnA = this.getRandomPhone()
        const msisdnB = this.getRandomPhone()
        const n = {
          webId,
          msisdnA,
          msisdnB
        };
        notificationRequests.push(n);
      }
      if (strategy === 'SIM') {
        this.sendSimultaneously(notificationRequests)
      } else if (strategy === 'SEQ') {
        this.sendSequentially(notificationRequests, delay)
      }
    },

    getRandomPhone() {
      const webId = this.$newWebId()
      return webId.substring(webId.length - 11, webId.length)
    },

    async sendSequentially(requests, delay) {
      for (const r of requests) {
        const response = await this.sendRequest(r)
        const { webId, status, statusCode } = response
        this.$root.$emit('onAddNotificationRequest', { ...r, webId, status, statusCode })
        await this.$sleep(delay)
      }
    },

    sendSimultaneously(requests) {
      // transform into bunch of promises
      requests.map(async r => {
        const response = await this.sendRequest(r)
        const { webId, status, statusCode } = response
        this.$root.$emit('onAddNotificationRequest', { ...r, webId, status, statusCode })
      })
    },

    async sendRequest(r) {
      let status = 'OK'
      let statusCode = 200
      const data = await this.$axios
        .$post('/unavailableSubscriber', r)
        .catch(e => {
          status = 'FAIL'
          statusCode = e.response.status
        })
      console.log(data)
      const { webId } = data
      return { webId, status, statusCode }
    }
  }
}
</script>
