<template>
  <el-container>
    <el-aside>
      <el-row class="mb-4" style="margin-left: 20px; margin-bottom: 20px;">
        <el-button @click="kbNew">
          <el-icon>
            <Plus />
          </el-icon>
        </el-button>
        <router-link to='/knowledgeCategory'>
          <el-button>
            <el-icon>
              <Collection />
            </el-icon>
          </el-button>
        </router-link>
      </el-row>

      <el-menu default-active="2" class="" :collapse="false">
        <el-menu-item @click="categorySwitch(null)">
          <template #title>全部</template>
        </el-menu-item>
        <el-menu-item v-for="item, index in categoryList" :index="index" :key="item.id" :label="item.name"
          @click="categorySwitch(item.id)">
          <template #title>{{ item.name }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main style="padding: 0;">
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
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
import api from '../../api'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

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

<style scoped>
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
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  grid-gap: 30px;
  justify-content: center;
  justify-items: stretch;
  margin-top: 30px;
  white-space: nowrap;
  padding: 0 30px;
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

.el-menu-item {
  height: 40px !important;
}

.el-aside {
  margin: 0 !important;
}

@media (max-width: 500px) {
  .el-aside {
    width: 40% !important;
    margin: 0 !important;
    padding: 0 !important;
    position: fixed;
    left: 0;
    height: 100%;
    z-index: 999;
  }

  .card-box {
    margin-left: calc(40% + 8px);
    margin-right: 0;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    grid-gap: 10px;
    justify-content: center;
    justify-items: stretch;
    margin-top: 10px;
    white-space: nowrap;
    margin-right: 10px;
    padding: 0;
  }

  .image {
    height: 50px;
    margin-top: 0;
  }

  .cover-image {
    width: 24px;
    height: 24px;
    margin-top: 10px;
  }

  .book-name {
    margin-left: 0;
    line-height: 20px;
    height: 20px;
  }

  .book-name>div {
    border-left: 1px solid #1d1e1f;
    line-height: 20px;
    height: 20px;
  }

  .card-edit {
    display: none;
  }

}
</style>