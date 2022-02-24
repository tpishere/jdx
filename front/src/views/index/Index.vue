<template>
  <div>
    <van-notice-bar
        v-if="notice && noticeModel == 'TOP'"
        left-icon="volume-o"
        :text="notice"
        mode="closeable"
    />

    <div>
      <!-- title -->
      <div
          v-if="title"
          style="text-align: center; margin: 40px 0 20px 0; font-size: 32px"
      >
        {{ title }}
      </div>

      <!-- sub title -->
      <div v-if="notice && noticeModel == 'HTML'" v-html="notice"></div>
    </div>

    <van-tabs v-model="active">
      <van-tab title="获取CK" name="jd">
        <JD/>
      </van-tab>
      <van-tab title="绑定WxPusher" name="bindWxPusher">
        <BindWxPusher/>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
import JD from "./JD"
import BindWxPusher from "./BindWxPusher"
import {baseInfo} from "@/api";

export default {
  name: "Index",
  components: {JD, BindWxPusher},
  data() {
    return {
      active: "jd",
      title: "",
      notice: "",
      noticeModel: "",
    }
  },
  mounted() {
    this.renderBase()
  },
  methods:{
    renderBase: function() {
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
}
</script>

<style scoped>

</style>