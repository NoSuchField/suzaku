import { createRouter, createWebHashHistory } from 'vue-router'

import Login from '../views/Login.vue'
import KnowledgeBaseList from '../views/knowledge/KnowledgeBaseList.vue'
import KnowledgeBaseCategoryList from '../views/knowledge/category/KnowledgeBaseCategoryList.vue'
import KnowledgeBaseAdd from '../views/knowledge/KnowledgeBaseAdd.vue'
import KnowledgeDetail from '../views/knowledge/KnowledgeBaseDetail.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      name: '/',
      component: KnowledgeBaseList
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/knowledgeCategory',
      name: 'KnowledgeBaseCategoryList',
      component: KnowledgeBaseCategoryList
    },
    {
      path: '/knowledgeNew',
      name: 'KnowledgeBaseAdd',
      component: KnowledgeBaseAdd
    },
    
    {
      path: '/knowledgeDetail',
      name: 'KnowledgeDetail',
      component: KnowledgeDetail
    }
  ]
})

export default router