<template>
  <div>
    <div>
      <van-field
          v-model="cookie"
          rows="3"
          autosize
          label="Cookie"
          type="textarea"
          placeholder="pt_key=xxx;pt_pin=xxx;"
      />
      <van-field
          v-model="wxPusherUid"
          rows="2"
          autosize
          label="UID"
          type="textarea"
          placeholder="UID_xxxxxx"
      />
      <div style="padding: 8px">
        <van-button round block type="primary" @click="bindWxPusher()">主要按钮</van-button>
      </div>
    </div>
  </div>
</template>

<script>
import {bindWXPusher} from "@/api";

export default {
  name: "BindWxPusher",
  data() {
    return {
      cookie: "",
      wxPusherUid: ""
    }
  },
  methods: {
    bindWxPusher: function () {
      let param = {cookie: this.cookie, wxPusherUid: this.wxPusherUid}
      bindWXPusher(param).then(resp => {
        console.log(resp)
      }).catch((err) => {
        console.error(err)
      })
    }
  }
}
</script>

<style scoped>

</style>