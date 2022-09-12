<template>
  <el-row class="mb-4">
    
      <el-button type="primary" @click="kbNew">
        <el-icon>
          <Plus />
        </el-icon>新建知识库
      </el-button>

    <router-link to='/knowledgeCategory'>
      <el-button type="primary">
        <el-icon>
          <Collection />
        </el-icon>分类管理
      </el-button>
    </router-link>
  </el-row>
  <el-row></el-row>

  <el-row>
    <el-radio-group v-model="category" size="large">
      <el-radio-button label="全部" @click="categorySwitch(null)" />
      <el-radio-button v-for="item in categoryList" :key="item.id" :label="item.name"
        @click="categorySwitch(item.id)" />
    </el-radio-group>
  </el-row>
  <div
    style="display: flex; flex-wrap: wrap; flex-grow: 1; justify-content: left">

    <el-card :body-style="{ padding: '0px' }" v-for="(item, index) in knowledgeBaseList" :key="item.id">
      <img :src="item.coverImage" class="image" @click="enterKb(item.id)"/>
      <div style="padding: 14px">
        <span>{{ item.name }}</span>
        <div class="bottom">

          <el-button text class="button">{{ item.description }}</el-button>
          <el-button :icon="Edit" size="large" @click="editPageMeta(item.id)" circle />
        </div>
      </div>
    </el-card>
  </div>

  <el-empty style="margin-top: 200px" description="暂无数据" v-if="knowledgeBaseList.length == 0"/>


</template>

<script lang="ts" setup>
import api from '../../api'
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const currentDate = ref(new Date())

import {
  Edit
} from '@element-plus/icons-vue'

interface Kb {
  id: string,
  name: string,
  description: string,
  coverImage: string
}

interface Category {
  id: string
  icon: string
  name: string
}

const router = useRouter()

let categoryList = ref<Category[]>([])

const category = ref('全部')
let categoryId = ref('0')

let knowledgeBaseList = ref<Kb[]>([])
api.post('/book/list', {
  "currentPage": 1,
  "pageSize": 100
}).then(response => {
  if (response.data.code === "200") {
    knowledgeBaseList.value = response.data.data.list;
  }
});

const lock = ref(false)

const enterKb = (id) => {
  router.push({'path': '/knowledgeDetail', 'query': {'id': id}});
}

const kbNew = () => {
  router.push({'path': '/knowledgeNew', 'query': {'category': categoryId.value}});
}

const categorySwitch = (id) => {
  categoryId.value = id
  if (!lock.value) {
    lock.value = true
    api.post('/book/list', id ? {
      "currentPage": 1,
      "pageSize": 100,
      "categoryId": id
    } : {
      "currentPage": 1,
      "pageSize": 100
    }).then(response => {
      lock.value = false
      if (response.data.code === "200") {
        knowledgeBaseList.value = response.data.data.list;
      }
    });
  }
}

api.post('/category/list', {}).then(response => {
  if (response.data.code === "200") {
    categoryList.value = response.data.data;
  }
});

const editPageMeta = (id) => {
  router.push({'path': '/knowledgeNew', 'query': {'id': id}});
}

</script>

<style>
.time {
  font-size: 12px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button {
  padding: 0;
  min-height: auto;
}

.el-card {

  margin-top: 30px;
  margin-bottom: 10px;

  margin-right: 30px;
  border: 4px solid #EFEFF4 !important;
  border-radius: 10px !important;
  cursor: pointer;
}

.el-card:hover {
  border: 4px solid yellow !important;
}

.image {
  width: 200px;
  height: 190px;
  border-bottom: 4px solid #EFEFF4;
  padding: 20px 50px;

}

.el-row {
  margin-top: 1rem;
}
</style>