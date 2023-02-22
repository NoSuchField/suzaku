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
  <div class="card-box">
    <el-card :body-style="{ padding: '0px' }" v-for="(item, index) in knowledgeBaseList" :key="item.id">
      <div>
        <div class="image">
          <div style="text-align: center; margin-left: 20px;">
            <img class="cover-image" :src="item.coverImage" @click="enterKb(item.id)" />
          </div>
          <div class="book-name" @click="enterKb(item.id)">
            <div>{{ item.name }}</div>
          </div>
          <div class="card-edit">
            <el-icon>
              <Edit @click="editPageMeta(item.id)" />
            </el-icon>
          </div>
        </div>
      </div>
    </el-card>
  </div>
  <el-empty style="margin-top: 200px" description="暂无数据" v-if="knowledgeBaseList.length == 0" />
</template>

<script lang="ts" setup>
import api from '../../api'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
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
  border: 1px solid #4C4D4F !important;
  border-radius: 6px !important;
  cursor: pointer;
}

.el-card:hover {
  border: 1px solid #FFFFFF !important;
}

.image {
  display: flex;
}

.card-box {
  padding: 0;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  grid-gap: 30px;
  justify-content: center;
  justify-items: stretch;
  margin-top: 30px;
}

.cover-image {
  width: 36px;
  height: 36px;
  margin-top: 20px;
  border: 1px solid #35363A;
  background-color: #EBEBEB;
  border-radius: 6px;
  padding: 2px;
}

.card-edit {
  font-size: 20px;
  color: #35363A;
  margin: 10px;
  flex-shrink: 1;
}

.card-edit:hover {
  color: #FFFFFF;
}

.book-name {
  line-height: 40px;
  flex-grow: 1;
  margin-left: 20px;
  border: 6px;
}

.book-name>div {
  margin: 20px auto;
  padding-left: 15px;
  border-left: 1px solid #35363A;
  font-weight: bold;
}

.el-row {
  margin-top: 1rem;
}
</style>