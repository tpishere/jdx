<template>
  <div>
    <van-notice-bar
        v-if="notice && noticeModel == 'TOP'"
        left-icon="volume-o"
        :text="notice"
        mode="closeable"
    />

    <!-- title -->
    <div>
      <div v-if="title" style="text-align: center; margin: 40px 0 20px 0; font-size: 32px">
        {{ title }}
      </div>

      <!-- sub title -->
      <div v-if="notice && noticeModel == 'HTML'" v-html="notice"></div>
    </div>

    <JD/>
  </div>
</template>

<script>
import JD from "./JD";
import {baseInfo} from "@/api";

export default {
  name: "Index",
  components: {JD},
  data() {
    return {
      title: "",
      notice: "",
      noticeModel: ""
    };
  },
  created() {
    this.renderBase();
    let tab = this.$route.query.tab
    if (tab) {
      this.active = tab
    } else {
      this.active = "jd"
    }
  },
  watch: {
  },
  methods: {
    renderBase: function () {
      baseInfo()
          .then(resp => {
            this.title = resp.data.title;
            this.notice = resp.data.notice;
            this.noticeModel = resp.data.noticeModel;
          })
          .catch(err => {
            console.log(err);
          });
    },
  }
};
</script>

<style scoped></style>
