<template>
  <div>
    <van-field
      v-model="form.mobile"
      name="mobile"
      label="手机号"
      placeholder="手机号"
    ></van-field>
    <van-field
      v-model="form.code"
      name="code"
      label="验证码"
      placeholder="验证码"
    >
      <template #button>
        <van-count-down
          v-if="Number(expireTime) > 0"
          ref="countDown"
          millisecond
          :time="expireTime"
          format="ss:SSS"
        />
        <van-button v-else size="small" plain type="info" @click="smsCode"
          >发送验证码
        </van-button>
      </template>
    </van-field>

    <div style="margin: 16px; ">
      <van-button
        round
        block
        :disabled="!form.code"
        type="primary"
        @click="login"
      >
        获取CK
      </van-button>
    </div>
  </div>
</template>
<script>
import { baseInfo, jdSmsCode, jdLogin } from "@/api";

export default {
  data() {
    return {
      expireTime: 0,
      form: {
        mobile: "",
        code: ""
      }
    };
  },
  mounted() {
    this.renderBase();
  },
  methods: {
    renderBase: function() {
      baseInfo()
        .then(resp => {
          this.title = resp.data.title;
          this.notice = resp.data.notice;
          this.noticeModel = resp.data.noticeModel;
          this.qls = resp.data.qls;
          if (this.qls.length > 0) {
            this.form.displayName = this.qls[0];
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    smsCode: function() {
      this.form.code = "";
      jdSmsCode(this.form.mobile).then(resp => {
        this.expireTime = resp.data.expireTime * 1000;
      });
    },
    login: function() {
      let _this = this;
      if (_this.model == "ck") {
        this.form.displayName = "";
        this.form.remark = "";
      }
      jdLogin(this.form)
        .then(function(response) {
          // 计时器清零
          _this.expireTime = 0;
          localStorage.setItem("ptPin", response.data.ptPin);
          // 弹框
          _this.$dialog
            .alert({
              title: "提示",
              message: response.data.cookie,
              confirmButtonText: "点击复制"
            })
            .then(() => {
              _this
                .$copyText(response.data.cookie)
                .then(() => {
                  _this.$toast.success("复制成功");
                  setTimeout(() => {
                    _this.$toast.success("如有需要，请选择并提交您的Cookie");
                    _this.$emit("change-tab", "submitQL");
                  }, 1000);
                })
                .catch(() => {
                  _this.$toast.fail("复制失败，请手动复制");
                });
            });
          if (_this.model == "ck") {
            // ignore
          }
          // ql模式
          if (_this.model == "ql") {
            // ignore
          }
        })
        .catch(function(error) {
          console.error(error);
        });
    }
  }
};
</script>
