<template>
  <div>
    <v-list disabled class="mb-4">
      <SmallTitle class="smalltitle-title">{{
        $t('instructions.title')
      }}</SmallTitle>

      <v-list-item-group>
        <v-list-item v-for="(item, i) in items" :key="i">
          <v-list-item-icon>
            <v-icon v-text="item.icon"></v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-subtitle v-text="item.text"></v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>
    <v-divider />
  </div>
</template>

<script>
export default {
  name: 'TestInstructions',

  data: app => ({
    items: [
      {
        text: app.$t('instructions.i1'),
        icon: 'mdi-numeric'
      },
      { text: app.$t('instructions.i2'), icon: 'mdi-clock' }
    ]
  })
}
</script>
