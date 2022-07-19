<template>
  <div class="title">
    <h5>
      <slot />
    </h5>
    <div class="underscore" />
  </div>
</template>
<script>
export default {
  name: 'SmallTitle'
}
</script>

<style scoped>
div.title {
  margin: 0 0 1.5rem 0;
}
div.underscore {
  border-bottom: 2px solid #007bff;
  width: 20px;
}
</style>
