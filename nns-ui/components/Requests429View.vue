<template>
    <div class="Requests429View">
        <div class="d-flex flex-row justify-space-between align-baseline">
            <SmallTitle v-if="title">{{ title }} (total - {{ total }})</SmallTitle>
        </div>

        <div class="d-flex flex-column">
            <v-data-iterator :items="items" :items-per-page="-1">
                <template #default="props">
                    <div class="d-flex flex-wrap">
                        <NotificationRequestItem v-for="item in props.items"
                            :key="item.webId" :item="item" />
                    </div>
                </template>
            </v-data-iterator>
        </div>

        <v-divider class="mt-8 mt-md-4" />
    </div>
</template>

<script>
export default {
    name: 'Requests429View',
    props: {
        title: {
            type: String,
            required: false,
            default: null
        },
        items: {
            type: Array,
            required: true
        }
    },

    computed: {
        total() {
            return this.items.length
        }
    },
}
</script>
