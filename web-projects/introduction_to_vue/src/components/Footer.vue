<template>
    <footer>
        <a href="/">Codeforces</a> {{ numOfUsers }} users and {{ numOfPosts }} posts
    </footer>
</template>

<script>
export default {
    name: "Footer",
    props: ["numOfPosts", "numOfUsers"],
}
</script>

<style scoped>

</style>