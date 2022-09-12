<script>
import { ref, toRaw } from 'vue'
import { useRoute, useRouter } from 'vue-router'
export default {
  "name": "App",
  setup() {
    const router = useRouter()

    let menuMap = new Map(Object.entries(
      {}
    ));

    let currentRoute = window.location.href.split('#')[1].split('?')[0].substring(1)

    let subActive = ref(currentRoute)

    let activeIndex = ref(currentRoute)

    let subMenuList = ref([])

    let asideWidth = ref("300px")

    let exp = window.localStorage.getItem('isExpand');

    let isExpand = ref((exp && exp == "true") ? true : false)

    let clickExpand = () => {
      let exp = !isExpand.value
      isExpand.value = exp
      window.localStorage.setItem('isExpand', exp)
      asideWidth.value = isExpand.value ? "300px" : "70px"
    }

    asideWidth.value = isExpand.value ? "300px" : "70px"

    let handleSelect = (key, keyPath) => {
      subMenuList.value = menuMap.get(key);
      let first = menuMap.get(key)[0];
      subActive.value = first.index
      router.push('/' + first.index);
    }

    const handleSubSelect = (key, keyPath) => {
      subActive.value = key;
      router.push('/' + key);
    }

    let token = window.localStorage.getItem("token");

    if (!token) {
      router.push("/login");
    } else {

      activeIndex.value = subActive.value

      subMenuList.value = menuMap.get(subActive.value);
      router.push("/" + subActive.value);

    }

    return {
      isExpand,
      clickExpand,
      asideWidth,
      activeIndex,
      handleSelect,
      subMenuList,
      menuMap,
      subActive,
      handleSubSelect
    }
  },
  data: () => {
    return {
    }
  },
  methods: {
    backHome() {
      this.$router.push("/");
    }
  }
}
</script>

<template>
  <div id="app" class="common-layout">
    <el-container>
      <el-container class="main-container">
        <el-aside :width="asideWidth" style="display: none">
          <div @click="clickExpand" id="clickExpand">
            <el-icon>
              <Menu />
            </el-icon>
          </div>

          <el-menu :default-active="subActive" background-color="#FFFFFF" text-color="#000000" @select="handleSubSelect"
            @open="handleOpen" @close="handleClose">
            <el-menu-item v-for="item in subMenuList" :key="item.id" :index="item.index">
              <el-icon>
                <icon-menu />
              </el-icon>
              <span v-if="isExpand">{{ item.name }}</span>
            </el-menu-item>
          </el-menu>

        </el-aside>


        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style>
.el-menu-item {
  padding: none !important;

}

.el-menu .el-icon {
  flex-shrink: 0;
}

#clickExpand {

  height: 50px;
  line-height: 50px;
  font-size: 50px;
  margin-left: 10px;
  margin-top: 6px;
  color: #909399;
  cursor: pointer;
}

#clickExpand:hover {

  color: #409eff;
}
</style>