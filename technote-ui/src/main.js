import { createApp } from 'vue'
import './style.css'

import router from '../src/router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import Markdown from 'vue3-markdown-it';
import App from './App.vue'

import 'highlight.js/styles/monokai.css';

const app = createApp(App)
app.use(Markdown)
app.use(router)
app.use(ElementPlus)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.mount('#app')
