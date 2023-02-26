<template>
    <div>
        <IndexPost v-for="post in posts" :post="post" :key="post.id" :user-login="users[post.userId].login" :num-of-comments="cntComments(post)"/>
    </div>
</template>

<script>
import IndexPost from "./IndexPost";

export default {
    name: "Index",
    props: ["posts", "users", "comments"],
    methods: {
      cntComments: function (post) {
        return Object.values(this.comments).filter(c => c.postId === post.id).length;
      }
    },
    components: {
      IndexPost
    }
}
</script>

<style scoped>

</style>
