<template>
    <div class="common-layout" style="height: calc(100%)">
        <el-container>
            <el-aside width="480px">
                <el-tree allow-drop="true" allow-drag="true" :highlight-current="true" ref="treeRef" :data="data" draggable
                    default-expand-all node-key="id" :expand-on-click-node=false @node-drag-start="handleDragStart"
                    @node-drag-enter="handleDragEnter" :current-node-key="currentNodeId" @node-drag-leave="handleDragLeave"
                    @node-drag-over="handleDragOver" @node-drag-end="handleDragEnd" @node-drop="handleDrop"
                    @node-click="handleNodeClick">
                    <template #default="{ node, data }">
                        <span class="custom-tree-node">
                            <span>{{ node.label }} </span>
                            <span>
                                <a id="btnDel">
                                    <el-icon @click="movePage('up', data.id)">
                                        <Top />
                                    </el-icon>

                                    <el-icon @click="movePage('down', data.id)">
                                        <Bottom />
                                    </el-icon>
                                    <el-icon @click="delPage.value = data; centerDialogVisible = true">
                                        <Delete />
                                    </el-icon>
                                </a>
                            </span>
                        </span>
                    </template>

                </el-tree>

                <div class="mt-4">

                    <el-input v-model="newPageName" placeholder="Please input" class="input-with-select">
                        <template #append>
                            <el-button :icon="Check" id="btnAddPage" type="success" @click="addPage" />
                        </template>
                    </el-input>
                </div>

            </el-aside>
            <el-container>
                <el-header class="navBar">

                    <el-row class="mb-4">
                        <el-button style="margin-left: 16px;" @click="viewIndex(true)">
                            返回
                        </el-button>

                        <el-button v-if="editMode" style="margin-left: 16px" @click="savePage">
                            保存
                        </el-button>

                        <el-button style="margin-left: 16px" @click="toggleMode" v-if="currentNodeId != 0">
                            {{ editMode == true ? "预览" : "编辑" }}
                        </el-button>
                    </el-row>


                    <el-breadcrumb :separator-icon="ArrowRight">
                        <el-breadcrumb-item v-for="item in pathList" @click="pageClicked(item)"
                            :class="item.id == currentNodeId ? 'currentBread' : ''">{{ item.label }}
                        </el-breadcrumb-item>
                    </el-breadcrumb>

                </el-header>
                <el-main>
                    <div class="mt-4" v-if="editMode" style="margin-bottom: 20px">
                        <el-input v-model="pageDetail.label" placeholder="Please input" class="input-with-select">
                        </el-input>
                    </div>

                    <MarkdownEditor v-if="editMode" v-model="pageDetail.content" />

                    <div v-if="!editMode && pageDetail.content && !showIndex" class="markdown-body">
                        <Markdown :source="pageDetail.content || ''" />
                    </div>

                    <el-tree v-if="showIndex" allow-drop="true" allow-drag="true" :highlight-current="true" ref="treeRef"
                        :data="data" draggable default-expand-all node-key="id" :expand-on-click-node=false
                        @node-drag-start="handleDragStart" @node-drag-enter="handleDragEnter"
                        @node-drag-leave="handleDragLeave" @node-drag-over="handleDragOver" @node-drag-end="handleDragEnd"
                        @node-drop="handleDrop" @node-click="handleNodeClick">
                        <template #default="{ node, data }">
                            <span class="custom-tree-node">
                                <span>{{ node.label }}</span>
                                <span>
                                    <a id="btnDel" style="margin-left: 8px"
                                        @click="delPage.value = data; centerDialogVisible = true">
                                        <el-icon>
                                            <Delete />
                                        </el-icon>
                                    </a>
                                </span>
                            </span>
                        </template>

                    </el-tree>

                    <el-dialog v-model="centerDialogVisible" title="Warning" width="30%" align-center>
                        <span>确定删除页面吗 ?</span>
                        <template #footer>
                            <span class="dialog-footer">
                                <el-button @click="centerDialogVisible = false">Cancel</el-button>
                                <el-button type="primary" @click="deletePage(delPage.value.id)">
                                    Confirm
                                </el-button>
                            </span>
                        </template>
                    </el-dialog>

                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script lang="ts" setup>
import api from '../../api'
import { ref, reactive, nextTick } from 'vue'
import { ElMessageBox } from 'element-plus'

import { Check, ArrowRight } from '@element-plus/icons-vue'

import { useRoute, useRouter } from 'vue-router'

import type Node from 'element-plus/es/components/tree/src/model/node'
import type { DragEvents } from 'element-plus/es/components/tree/src/model/useDragNode'
import type { NodeDropType } from 'element-plus/es/components/tree/src/tree.type'

import { ElTree } from 'element-plus'

import Markdown from 'vue3-markdown-it';

import 'github-markdown-css';

import MarkdownEditor from '../../components/MarkdownEditor.vue'

const router = useRouter()

let query = {}

if (localStorage.getItem('query')) {
    query = JSON.parse(localStorage.getItem('query'))
} else {
    query = router.currentRoute.value.query
}

if (router.currentRoute.value.query.id) {
    query = router.currentRoute.value.query
    localStorage.setItem('query', JSON.stringify(router.currentRoute.value.query))
} else {
    router.push({ 'path': '/knowledgeDetail', 'replace': true, 'query': query });
}


let id = query.id

interface KbMetaData {
    id: string,
    name: string,
    description: string,
    coverImage: string
}

interface TreeNode {
    id: string,
    label: string
}

let centerDialogVisible = ref(false)

let pathList = ref<TreeNode[]>([{ 'id': '0', 'label': 'root' }])

let kbMetaData = ref<KbMetaData>()

let bookUrl = location.href.split('&')[0]

api.get('/book/detail?id=' + id).then(response => {
    if (response.data.code === "200") {
        kbMetaData.value = response.data.data
        viewIndex(false)
    }
});

const cmOptions = {
    mode: "text/x-markdown", // Language mode
    theme: "material-darker", // Theme
    tabSize: 4,
    cursorBlinkRate: 1000,
    smartIndent: true, // Smart indent
    indentUnit: 4, // The smart indent unit is 2 spaces in length
    lineNumbers: true,
    foldGutter: true, // Code folding
    styleActiveLine: true, // Display the style of the selected row
}

const cmChange = () => {

}

const delPage = ref({})

let currentNode = ref({ 'id': '0', 'label': 'root' })

const pageClicked = (node) => {
    let nodeId = node.id
    currentNode.value = node

    setCheckedNodes(node)

    let newPathList = []

    let flag = false
    pathList.value.forEach(p => {
        if (!flag) {
            newPathList.push(p)
        }
        if (p.id == node.id) {
            flag = true
            pathList.value = newPathList

        }
    })
    localStorage.setItem('path', JSON.stringify(pathList.value))
    refreshPage(nodeId)
}


const addPage = () => {
    let data = {
        bookId: id,
        parentId: currentNodeId.value,
        label: newPageName.value
    }
    api.post('/page/add', data).then(response => {
        if (response.data.code === "200") {
            let query = JSON.parse(localStorage.getItem('query'))
            query.pageId = response.data.data
            router.replace({ 'path': '/knowledgeDetail', 'replace': false, 'query': query });
            localStorage.setItem('query', JSON.stringify(query))

            if (id != 0) {
                let newPathList = JSON.parse(localStorage.getItem('path'))
                newPathList.push({ 'id': response.data.data, 'label': newPageName.value })
                localStorage.setItem('path', JSON.stringify(newPathList))
            }

            nextTick(() => { router.go(0) })
        }
    });
}

interface PageDetail {
    id: string,
    name: string,
    description: string,
    coverImage: string,
    content: string
}


const pageDetail = ref<PageDetail>({
    id: '0',
    name: 'root',
    description: '',
    coverImage: '',
    content: ''
})

const treeRef = ref<InstanceType<typeof ElTree>>()

const setCheckedNodes = (node) => {

    let id = node.id === "0" ? null : node.id
    if (treeRef.value!) {
        treeRef.value!.setCurrentKey(
            id,
            true
        )
    }
}

const deletePage = (pageId) => {
    api.post('/page/delete?id=' + pageId).then(response => {
        if (response.data.code === "200") {
            localStorage.removeItem('query')
            delPage.value = {}
            centerDialogVisible.value = false

            pathList.value = [{ 'id': '0', 'label': 'root' }]
            localStorage.setItem('path', JSON.stringify(pathList.value))

            localStorage.setItem('query', JSON.stringify({ 'id': id }))

            router.push({ 'path': '/knowledgeDetail', 'replace': true, 'query': { 'id': id } });

            nextTick(() => {

                api.get('/book/detail?id=' + id).then(response => {
                    if (response.data.code === "200") {
                        kbMetaData.value = response.data.data
                        viewIndex(false)
                        showIndex.value = true
                        currentNodeId.value = 0
                    }
                });
            })
        }
    });
}

let currentNodeId = ref(0)

const refreshPage = (nodeId) => {

    if (nodeId == 0) {
        showIndex.value = true
        currentNodeId.value = nodeId
    } else {
        showIndex.value = false
        currentNodeId.value = nodeId
        api.get('/page/detail?id=' + nodeId).then(response => {
            if (response.data.code === "200") {
                pageDetail.value = response.data.data
            }
        });
    }
    const newUrl = nodeId == 0 ? bookUrl : `${bookUrl}&pageId=${nodeId}`
    // window.history.replaceState('', '', newUrl)
    let query = { 'id': id, 'pageId': nodeId }
    localStorage.setItem('query', JSON.stringify(query))
    router.push({ 'path': '/knowledgeDetail', 'replace': true, 'query': query });

    let flag = false
    pathList.value = JSON.parse(localStorage.getItem('path'))

    console.log(treeRef.value)

}
let editMode = ref(false)

const toggleMode = () => {
    editMode.value = !editMode.value

}

const handleDragStart = (node: Node, ev: DragEvents) => {
}
const handleDragEnter = (
    draggingNode: Node,
    dropNode: Node,
    ev: DragEvents
) => {
}
const handleDragLeave = (
    draggingNode: Node,
    dropNode: Node,
    ev: DragEvents
) => {
}
const handleDragOver = (draggingNode: Node, dropNode: Node, ev: DragEvents) => {
}
const handleDragEnd = (
    draggingNode: Node,
    dropNode: Node,
    dropType: NodeDropType,
    ev: DragEvents
) => {
}
const handleDrop = (
    draggingNode: Node,
    dropNode: Node,
    dropType: NodeDropType,
    ev: DragEvents
) => {
}

const handleNodeClick = (
    node,
    prop,
    ev
) => {
    showIndex.value = false
    refreshPage(node.id)
    currentNode.value = { 'id': node.id, 'label': node.label }
    pathList.value = [{ id: '0', label: 'root' }].concat(node.path)
    localStorage.setItem('path', JSON.stringify(pathList.value))
}

const allowDrop = (draggingNode: Node, dropNode: Node, type: NodeDropType) => {
    if (dropNode.data.label === 'Level two 3-1') {
        return type !== 'inner'
    } else {
        return true
    }
}
const allowDrag = (draggingNode: Node) => {
    return !draggingNode.data.label.includes('Level three 3-1-1')
}

let showIndex = ref(true)

const newPageOption = ref()

let data = ref<Node[]>()

const viewIndex = (draw) => {
    if (draw == true) {
        router.replace("/")
    }
    api.get('/page/list?bookId=' + kbMetaData.value?.id).then(response => {
        if (response.data.code === "200") {
            data.value = response.data.data

            if (treeRef.value!) {
                treeRef.value!.setCurrentKey(
                    currentNodeId,
                    true
                )
            }
        }
    });
}

const savePage = () => {
    api.post('/page/update', pageDetail.value).then(response => {
        if (response.data.code === "200") {
            router.go(0)
        }
    });
}

const newPageName = ref('')


const drawer = ref(false)
const drawer2 = ref(false)
const direction = ref('ltr')
const radio1 = ref('Option 1')
const handleClose = (done: () => void) => {
    done()

}
function cancelClick() {
    drawer2.value = false
}
function confirmClick() {
    drawer2.value = false
}

if (query.pageId) {
    refreshPage(query.pageId)
}


const movePage = (direction, pageId) => {
    api.post('/page/move/' + direction + "?pageId=" + pageId).then(response => {
        if (response.data.code === "200") {
            router.go(0)
        }
    });
}

</script>

<style>
.el-drawer__body {
    padding: 20px !important;
}

.el-tree-node__content {
    height: 40px !important;
    line-height: 40px !important;
    border-radius: 10px;
}

.el-tree {
    background: #242424 !important;
    font-size: 18px;
}

.el-drawer.ltr {
    height: calc(100% - 60px) !important;
    top: 60px !important;
}

.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
}

.custom-tree-node a:hover {
    color: green;
}

#btnDel {
    color: #242424;
    font-size: 22px;
    width: 0
}

#btnDel:hover {
    color: #D3D3D3;
    width: auto
}

#btnDel>.el-icon {
    font-size: 16px;
}

#btnDel>.el-icon :hover {
    color: #808080;
    background-color: #333333;
    border-radius: 3px
}

#btnAdd {
    color: #2B6298;
}

#btnAddPage {
    font-size: 15px;
}

#btnAddPage:hover {
    color: green;
}

.el-breadcrumb {
    margin: 20px auto;
    background-color: #252526;
    border: 1px solid #252525;
    line-height: 40px;
    height: 40px;
    padding: auto 20px;
    border-radius: 5px;
}

.el-breadcrumb__item {
    line-height: 40px;
}

.el-breadcrumb__inner {
    padding-left: 1rem;
    padding-right: 1rem;
}

.el-breadcrumb__inner:hover {
    color: #EFEFEF !important;
    background-color: #1E1E1E;
    cursor: pointer !important;
}

.el-breadcrumb__item:first-child {
    line-height: 40px;
}

.currentBread {
    color: #EFEFEF !important;
    background-color: #1E1E1E;
}

.markdown-body {
    padding: 30px;
    border-radius: 5px;
}

.navBar {
    height: 140px !important;
    background-color: #292A2D;
    padding: 20px;
    z-index: 999;
    margin: 20px;
    margin-top: 30px;
    border-radius: 5px;
    padding-bottom: 0 !important;
}

.el-main {
    flex-basis: 0 !important;
    padding-top: 0 !important;
}

.cm-editor,
.cm-gutters {
    background-color: #1E1E1E !important;
}

.cm-line {
    border-top: 2px solid transparent !important;
    border-bottom: 2px solid transparent !important;
    color: #D4D4D4;
}

.cm-activeLine,
.cm-activeLineGutter {
    background-color: #1E1E1E !important;
    border-top: 2px solid #282828 !important;
    border-bottom: 2px solid #282828 !important;
}

.ͼc {
    color: #D0B984;
}

.el-aside {
    padding: 10px;
    margin-right: 0;
}

.cm-gutterElement {
    display: none !important;
}

.cm-content {
    margin: 15px !important;
}

.markdown-body {
    color: #A8B7C5;
    --color-canvas-default: #2B2B2B !important;
    border: 1px solid #404040 !important;
}

.markdown-body blockquote {
    background-color: #3C3D3E;
    padding: .8rem;
    color: #A8B7C5;
    border-left: 2px solid #476387;
}

.markdown-body code {
    border-radius: 4px;
    background-color: #3C3D3E;
    padding: .2rem;
}

.markdown-body img {
    max-height: 200px;
    border: 1px solid #282828;
}

.markdown-body pre {
    background-color: #3C3D3E;
    padding: 0;
    border-radius: 4px;
}

.markdown-body a {
    color: #589DF6;
}

.markdown-body table tr:nth-child(2n) {
    background-color: #323232;
}
</style>