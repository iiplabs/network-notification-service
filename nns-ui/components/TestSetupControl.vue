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

      <div class="d-flex flex-column flex-sm-row">
        <v-radio-group v-model="strategy" :label="$t('tests_setup.strategy.title')" row>
          <v-radio :label="$t('tests_setup.strategy.options.sequentially')" value="SEQ"></v-radio>
          <v-radio :label="$t('tests_setup.strategy.options.simultaneously')" value="SIM"></v-radio>
        </v-radio-group>
        <v-text-field v-if="sequential" v-model.number="delay" type="number" :label="$t('tests_setup.delay')"
          suffix="ms" class="ml-sm-8 TestSetupControl-Delay"></v-text-field>
      </div>

      <v-btn :small="$vuetify.breakpoint.xsOnly" color="primary" outlined :disabled="sliderVal === 0 || showProgress"
        :loading="showProgress" class="mx-auto" @click="enterValue">
        {{ $t('tests_setup.button') }}
        <template #loader>
          <span class="cmac-loader">
            <v-icon light>mdi-cached</v-icon>
          </span>
        </template>
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
    }
  },

  methods: {
    enterValue() {
      const { delay, sliderVal, strategy } = this;
      this.$emit("controlUpdated", {
        testsNumber: sliderVal,
        strategy,
        delay
      });
    }
  }
}
</script>

<style scoped>
.TestSetupControl-Delay {
  max-width: 8rem;
}
</style>
