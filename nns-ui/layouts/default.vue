<template>
  <v-app>
    <TopBar />
    <v-content>
      <v-fade-transition mode="out-in">
        <nuxt />
      </v-fade-transition>
    </v-content>
  </v-app>
</template>

<style lang="scss">
html {
  @media all and (max-width: 570px) {
    font-size: 0.9rem;

    .v-label {
      font-size: 0.9rem;
    }
    .v-input {
      font-size: 0.9rem;
    }
  }

  font-size: 1rem;
}
</style>
