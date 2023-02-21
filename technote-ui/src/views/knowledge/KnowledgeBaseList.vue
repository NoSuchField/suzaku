<template>
  <el-row class="mb-4">
    <el-button type="primary" @click="kbNew">
      <el-icon>
        <Plus />
      </el-icon>
      新建知识库
    </el-button>
    <router-link to='/knowledgeCategory'>
      <el-button type="primary">
        <el-icon>
          <Collection />
        </el-icon>
        分类管理
      </el-button>
    </router-link>
  </el-row>
  <el-row></el-row>

  <el-row>
    <el-radio-group v-model="category" size="large">
      <el-radio-button label="全部" @click="categorySwitch(null)" />
      <el-radio-button v-for="item in categoryList" :key="item.id" :label="item.name" @click="categorySwitch(item.id)" />
    </el-radio-group>
  </el-row>
  <div style="display: flex; flex-wrap: wrap; flex-grow: 1; justify-content: left">
    <el-card :body-style="{ padding: '0px' }" v-for="(item, index) in knowledgeBaseList" :key="item.id">
      <div>
        <div class="image">
          <img :src="item.coverImage" @click="enterKb(item.id)" />
          <div class="card-edit">
            <el-icon>
              <Edit @click="editPageMeta(item.id)" />
            </el-icon>
          </div>
        </div>
        <div class="book-name">
          <span>{{ item.name }}</span>
        </div>
      </div>
    </el-card>
  </div>
  <el-empty style="margin-top: 200px" description="暂无数据" v-if="knowledgeBaseList.length == 0" />
</template>

<script lang="ts" setup>
import api from '../../api'
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const currentDate = ref(new Date())

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
  router.push({ 'path': '/knowledgeDetail', 'query': { 'id': id } });
}

const kbNew = () => {
  router.push({ 'path': '/knowledgeNew', 'query': { 'category': categoryId.value } });
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
  router.push({ 'path': '/knowledgeNew', 'query': { 'id': id } });
}

</script>

<style>
.el-card {
  margin-top: 30px;
  margin-bottom: 10px;
  margin-right: 30px;
  border: 2px solid #35363A !important;
  border-radius: 6px !important;
  cursor: pointer;
}

.el-card:hover {
  border: 2px solid #41B883 !important;
}

.image {
  display: flex;
  border-bottom: 1px dashed #35363A;
}

.image>img {
  width: 40px;
  height: 40px;
  padding: 20px 50px;
  padding-left: 60px;
  margin-left: 20px;
}

.card-edit {
  font-size: 20px;
  color: #35363A;
  margin: 10px;
}

.card-edit:hover {
  color: #41B883;
}

.book-name {
  margin: 8px 16px;
}

.el-row {
  margin-top: 1rem;
}
</style>