<template>
  <div>
    <!-- 网站设置 -->
    <van-divider
      :style="{
        color: '#1989fa',
        borderColor: '#1989fa',
        padding: '0 16px',
        marginTop: '32px'
      }"
      >网站设置
    </van-divider>
    <van-swipe-cell>
      <van-cell-group inset>
        <van-cell title="标题" :value="title" clickable />
        <van-cell title="公告" :value="notice" clickable />
        <van-cell title="公告模式" :value="noticeModel" clickable />
      </van-cell-group>

      <template #right>
        <van-button
          square
          type="info"
          class="slide-button"
          text="编辑"
          @click="webSiteConfigVisible = true"
        />
      </template>
    </van-swipe-cell>
    <van-action-sheet v-model="webSiteConfigVisible" title="编辑网站设置">
      <van-form>
        <van-field v-model="title" label="网站标题" placeholder="网站标题" />
        <van-field
          v-model="notice"
          label="公告"
          autosize
          type="textarea"
          placeholder="公告"
        />
        <van-field
          v-model="noticeModel"
          label="公告模式"
          @click="noticeModelPicker.show = true"
        >
          <van-popup v-model="noticeModelPicker.show" position="bottom">
            <van-picker
              show-toolbar
              :columns="noticeModelPicker.supportModel"
              @cancel="showPicker = false"
            />
          </van-popup>
        </van-field>
        <div style="margin: 16px;">
          <van-button round block type="info" @click="updateWebsiteConfig()"
            >提交
          </van-button>
        </div>
      </van-form>
    </van-action-sheet>
    <!-- 网站设置 end -->

    <!-- 定时任务配置 -->
    <van-divider
      :style="{
        color: '#1989fa',
        borderColor: '#1989fa',
        padding: '0 16px',
        marginTop: '32px'
      }"
      >定时任务配置
    </van-divider>
    <!-- cookie -->
    <van-swipe-cell>
      <van-cell-group inset>
        <van-cell title="检查Cookie Cron" :value="checkCookie.cron" clickable />
      </van-cell-group>
      <template #right>
        <van-button
          square
          type="info"
          class="slide-button"
          text="编辑"
          @click="checkCookie.picker.show = true"
        />
        <van-button
          square
          type="primary"
          class="slide-button"
          text="执行"
          @click="doCheckCookie"
        />
      </template>
    </van-swipe-cell>
    <van-action-sheet
      v-model="checkCookie.picker.show"
      title="编辑定时检查Cookie Cron"
    >
      <van-form>
        <van-field v-model="checkCookie.cron" label="Cron" placeholder="cron" />
        <div style="margin: 16px;">
          <van-button round block type="info" @click="updateCheckCookieCron()"
            >提交
          </van-button>
        </div>
      </van-form>
    </van-action-sheet>

    <van-popup
      v-if="checkCookie.resultPop.show && checkCookie.resultPop.data.length > 0"
      v-model="checkCookie.resultPop.show"
      title="以下Cookie已经过期"
      :style="{ width: '85%', height: '80%' }"
      closeable
    >
      <div style="font-weight: bold; text-align: center; margin: 16px 0">
        以下Cookie已过期，已自动禁用
      </div>
      <van-list>
        <van-cell v-for="d in checkCookie.resultPop.data" :key="d.displayName">
          <template slot="default">
            <div>
              <div style="font-weight: bold">节点：{{ d.displayName }}</div>
              <div
                v-for="ck in d.expiredPtPins"
                :key="ck"
                style="padding: 4px 0 0 16px "
              >
                <span>{{ ck }}</span>
              </div>
            </div>
          </template>
        </van-cell>
      </van-list>
    </van-popup>
    <!-- cookie end -->
    <!-- 定时任务配置 end -->

    <!-- 社交登录设置 -->
    <van-divider
      :style="{
        color: '#1989fa',
        borderColor: '#1989fa',
        padding: '0 16px',
        marginTop: '48px'
      }"
      >社交登录设置
    </van-divider>
    <div style="margin: 16px 0 16px 20px">
      <van-popover
        v-model="showPopover"
        trigger="click"
        :actions="socialPicker.actions"
        @select="editSocialConfig()"
      >
        <template #reference>
          <van-button type="primary" icon="apps-o" size="small"
            >操作
          </van-button>
        </template>
      </van-popover>
    </div>

    <van-swipe-cell
      v-for="social in socialPlatforms"
      :key="social.source"
      :title="social.source"
      style="margin-bottom: 20px"
    >
      <van-cell-group :title="social.source" inset>
        <van-cell title="客户端ID" clickable>
          <template #default>
            {{ social.clientId }}
          </template>
        </van-cell>
        <van-cell title="客户端密钥" clickable>
          <template #default>
            {{ social.clientSecret }}
          </template>
        </van-cell>
        <van-cell title="重定向地址" clickable>
          <template #default>
            {{ social.redirectUri }}
          </template>
        </van-cell>
        <van-cell title="管理员" clickable>
          <template #default>
            {{ social.admin }}
          </template>
        </van-cell>
      </van-cell-group>
      <template #right>
        <van-button
          square
          text="编辑"
          type="info"
          class="slide-button"
          @click="editSocialConfig(social)"
        />
        <van-button
          square
          text="删除"
          type="danger"
          class="slide-button"
          @click="delSocialConfig(social.source)"
        />
      </template>
    </van-swipe-cell>

    <van-action-sheet
      v-model="saveSocialConfigVisible"
      :title="saveSocialTitle"
    >
      <van-form>
        <van-field
          :value="form.socialConfig.source"
          label="社交平台"
          placeholder="点击选择社交平台"
          @click="socialPicker.show = true"
        />
        <van-popup v-model="socialPicker.show" position="bottom">
          <van-picker
            :readonly="socialPicker.readonly"
            :title="socialPicker.title"
            show-toolbar
            :columns="supportedSocialSource"
            @confirm="conformSocial"
            @cancel="socialPicker.show = false"
          />
        </van-popup>
        <van-field
          v-model="form.socialConfig.clientId"
          label="客户端ID"
          type="textarea"
          autosize
          placeholder="客户端ID"
        />
        <van-field
          v-model="form.socialConfig.clientSecret"
          label="客户端密钥"
          type="textarea"
          autosize
          placeholder="客户端密钥"
        />
        <van-field
          v-model="form.socialConfig.redirectUri"
          label="重定向地址"
          type="textarea"
          autosize
          placeholder="重定向地址"
        />
        <van-field
          v-model="form.socialConfig.admin"
          label="管理员"
          type="textarea"
          autosize
          placeholder="多个英文半角逗号 , 分割"
        />
        <div style="margin: 16px;">
          <van-button round block type="info" @click="saveSocialConfig()"
            >提交
          </van-button>
        </div>
      </van-form>
    </van-action-sheet>
    <!-- 社交登录设置 end -->
  </div>
</template>

<script>
import {
  getSystemConfig,
  updateWebsiteConfig,
  saveSocialConfig,
  delSocialConfig,
  checkCookie,
  updateCheckCookieCron
} from "@/api/admin";

export default {
  name: "SystemConfig",
  data() {
    return {
      title: "",
      notice: "",
      noticeModel: "",
      checkCookie: {
        cron: "",
        picker: {
          show: false
        },
        resultPop: {
          show: false,
          data: []
        }
      },
      socialPlatforms: [
        {
          source: "",
          clientId: "",
          clientSecret: "",
          redirectUri: "",
          admin: ""
        }
      ],

      webSiteConfigVisible: false,
      saveSocialConfigVisible: false,

      noticeModelPicker: {
        show: false,
        title: "编辑公告模式",
        supportModel: ["TOP", "HTML"]
      },

      showPopover: false,
      saveSocialTitle: "编辑社交登录设置",
      socialPicker: {
        title: "选择社交平台",
        show: false,
        actions: [{ text: "新增配置" }],
        readonly: false
      },
      form: {
        socialConfig: {
          source: "",
          clientId: "",
          clientSecret: "",
          redirectUri: "",
          admin: ""
        }
      },
      supportedSocialSource: ["GITHUB", "GITEE"]
    };
  },
  mounted() {
    this.getSystemConfig();
  },
  methods: {
    getSystemConfig: function() {
      getSystemConfig().then(resp => {
        this.title = resp.data.title;
        this.notice = resp.data.notice;
        this.noticeModel = resp.data.noticeModel;
        this.checkCookie.cron = resp.data.checkCookieCron;
        this.socialPlatforms = resp.data.socialPlatforms;
      });
    },
    updateWebsiteConfig: function() {
      let param = {};
      param.title = this.title;
      param.notice = this.notice;
      param.noticeModel = this.noticeModel;
      updateWebsiteConfig(param).then(resp => {
        this.title = resp.data.title;
        this.notice = resp.data.notice;
        this.webSiteConfigVisible = false;
      });
    },
    conformSocial: function(value) {
      this.form.socialConfig.source = value;
      this.socialPicker.show = false;
    },

    editSocialConfig: function(param) {
      if (param) {
        this.form.socialConfig = param;
        this.saveSocialTitle = "编辑社交登录设置";
        this.socialPicker.readonly = true;
        this.socialPicker.title = "无法编辑社交平台";
        this.saveSocialConfigVisible = true;
      } else {
        this.socialPicker.title = "选择社交平台";
        this.saveSocialTitle = "新增社交登录设置";
        this.form.socialConfig = {
          source: "",
          clientId: "",
          clientSecret: "",
          redirectUri: "",
          admin: ""
        };
        this.saveSocialConfigVisible = true;
      }
    },
    saveSocialConfig: function() {
      saveSocialConfig(this.form.socialConfig).then(resp => {
        this.form.socialConfig.source = "";
        this.form.socialConfig.clientId = "";
        this.form.socialConfig.clientSecret = "";
        this.form.socialConfig.redirectUri = "";

        this.saveSocialConfigVisible = false;
        this.socialPicker.readonly = false;
        this.socialPlatforms = resp.data;
      });
    },
    delSocialConfig: function(source) {
      this.$dialog
        .confirm({
          title: "提示",
          message: "确定删除么?"
        })
        .then(() => {
          delSocialConfig(source).then(resp => {
            this.socialPlatforms = resp.data;
          });
        })
        .catch(() => {
          this.$dialog.close();
        });
    },
    doCheckCookie: function() {
      checkCookie().then(resp => {
        if (resp.data.length > 0) {
          this.checkCookie.resultPop.show = true;
          this.checkCookie.resultPop.data = resp.data;
        } else {
          this.$dialog.alert({
            title: "提示",
            message: "所有Cookie均正常!"
          });
        }
      });
    },
    updateCheckCookieCron: function() {
      let param = { cron: this.checkCookie.cron };
      updateCheckCookieCron(param).then(() => {
        this.checkCookie.picker.show = false;
      });
    }
  }
};
</script>

<style scoped></style>
