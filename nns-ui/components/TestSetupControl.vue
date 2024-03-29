<template>
  <div>
    <div class="d-flex flex-row justify-space-between align-baseline">
      <SmallTitle v-if="title">{{ title }}</SmallTitle>
      <SocketInfo v-if="socketInfo" :info="socketInfo" />
    </div>

    <div class="mt-4 d-flex flex-column">
      <v-slider v-model="sliderVal" :label="$t('tests_setup.number_of_notifications')" thumb-label="always" :max="max"
        :min="min">
      </v-slider>

      <div class="d-flex flex-column flex-sm-row my-2">
        <v-btn class="ma-2" outlined @click="setRequests(1)">
          1
        </v-btn>
        <v-btn class="ma-2" outlined @click="setRequests(5)">
          5
        </v-btn>
        <v-btn class="ma-2" outlined @click="setRequests(10)">
          10
        </v-btn>
        <v-btn class="ma-2" outlined @click="setRequests(100)">
          100
        </v-btn>
        <v-btn class="ma-2" outlined @click="setRequests(200)">
          200
        </v-btn>
        <v-btn class="ma-2" outlined @click="setRequests(500)">
          500
        </v-btn>
        <v-btn class="ma-2" outlined @click="setRequests(1000)">
          1000
        </v-btn>
        <v-btn class="ma-2" outlined @click="setRequests(2000)">
          2000
        </v-btn>
      </div>

      <div class="d-flex flex-column flex-sm-row">
        <v-radio-group v-model="strategy" :label="$t('tests_setup.strategy.title')" row>
          <v-radio :label="$t('tests_setup.strategy.options.sequentially')" value="SEQ"></v-radio>
          <v-radio :label="$t('tests_setup.strategy.options.simultaneously')" value="SIM"></v-radio>
        </v-radio-group>
        <v-text-field v-if="sequential" v-model.number="delay" type="number" :label="$t('tests_setup.delay')"
          suffix="ms" class="ml-sm-8 TestSetupControl-Delay"></v-text-field>
      </div>

      <v-btn :small="$vuetify.breakpoint.xsOnly" color="primary" outlined
        :disabled="sliderVal === 0 || showProgress || !socketConnected" :loading="showProgress" class="mx-auto"
        @click="enterValue">
        {{ $t('tests_setup.button') }}
      </v-btn>
    </div>

    <v-divider class="mt-4" />
  </div>
</template>

<script>
export default {
  name: "TestSetupControl",
  props: {
    title: {
      type: String,
      required: false,
      default: null
    },
    min: {
      type: Number,
      required: false,
      default: 1
    },
    max: {
      type: Number,
      required: false,
      default: 100
    },
    showProgress: {
      type: Boolean,
      required: false,
      default: false
    },
    socketInfo: {
      type: Object,
      required: false,
      default: null
    },
  },

  data: () => ({
    delay: 0,
    sliderVal: 10,
    strategy: 'SEQ'
  }),

  computed: {
    sequential() {
      return this.strategy === 'SEQ'
    },

    socketConnected() {
      let connected = false
      const { socketInfo } = this
      if (socketInfo) {
        const { status } = socketInfo
        connected = status && status === 'CONNECTED'
      }
      return connected
    }
  },

  methods: {
    setRequests(requestsNum) {
      this.sliderVal = requestsNum
    },

    enterValue() {
      const { delay, sliderVal, strategy } = this;
      this.$emit("controlUpdated", {
        testsNumber: sliderVal,
        strategy,
        delay
      })
    }
  }
}
</script>

<style scoped>
.TestSetupControl-Delay {
  max-width: 8rem;
}
</style>
