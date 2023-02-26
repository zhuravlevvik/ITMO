<template>
  <article>
    <div class="title">{{ post.title }}</div>
    <div class="information">By {{ userLogin }} </div>
    <div class="body">{{ post.text }}</div>
    <div class="footer">
      <div class="left">
        <img src="../../assets/img/voteup.png" title="Vote Up" alt="Vote Up"/>
        <span class="positive-score">+173</span>
        <img src="../../assets/img/votedown.png" title="Vote Down" alt="Vote Down"/>
      </div>
      <div class="right">
        <img src="../../assets/img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
        <img src="../../assets/img/comments_16x16.png" title="Comments" alt="Comments"/>
        <a href="#">{{ numOfComments }}</a>
      </div>
    </div>
  </article>
</template>

<script>
export default {
  name: "IndexPost",
  props: ["post", "userLogin", "numOfComments"],
}
</script>

<style scoped>

</style>