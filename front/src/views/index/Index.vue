<template>
  <div>
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

export default {
  name: "Index",
  components: {JD,BindWxPusher},
  data() {
    return {active: "jd"}
  }
}
</script>

<style scoped>

</style>