<template>
  <div>
    <v-app-bar app dense flat outlined>
      <v-toolbar-title v-if="$vuetify.breakpoint.smAndUp">
        {{ $t('title') }}
      </v-toolbar-title>
      <div v-else>
        {{ $t('title') }}
      </div>

      <v-spacer></v-spacer>

      <div class="nns-lang">
        <NuxtLink
          :to="switchLocalePath('en')"
          class="nns-lang-selector hvr-underline-from-left"
          exact
          >EN</NuxtLink
        >
        <span class="nns-lang-selector nns-lang-spacer">/</span>
        <NuxtLink
          :to="switchLocalePath('fr')"
          class="nns-lang-selector hvr-underline-from-left"
          exact
          >FR</NuxtLink
        >
      </div>
    </v-app-bar>
  </div>
</template>

<script>
export default {
  name: 'TopBar',
}
</script>

<style scoped>
a,
a:visited {
  color: black;
  text-decoration: none;
}
a.nuxt-link-exact-active {
  color: black;
}
</style>
