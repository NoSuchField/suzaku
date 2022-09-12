<template>

    <div class="common-layout" style="height: calc(100%)">
        <el-container>
            <el-aside width="300px">



                <el-tree allow-drop="true" allow-drag="true" :highlight-current="true" ref="treeRef" :data="data"
                    draggable default-expand-all node-key="id" :expand-on-click-node=false
                    @node-drag-start="handleDragStart" @node-drag-enter="handleDragEnter"
                    @node-drag-leave="handleDragLeave" @node-drag-over="handleDragOver" @node-drag-end="handleDragEnd"
                    @node-drop="handleDrop" @node-click="handleNodeClick">
                    <template #default="{ node, data }">
                        <span class="custom-tree-node">
                            <span>{{ node.label }}</span>
                            <span>

                                <a id="btnDel" style="margin-left: 8px">
                                    <el-icon>
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
                        <el-button type="primary" style="margin-left: 16px;" @click="viewIndex(true)">
                            查看目录
                        </el-button>

                        <el-button type="primary" style="margin-left: 16px" @click="savePage">
                            保存
                        </el-button>

                        <el-button type="primary" style="margin-left: 16px" @click="toggleMode">
                            {{ editMode == true ? "预览" : "编辑" }}
                        </el-button>
                    </el-row>


                    <el-breadcrumb :separator-icon="ArrowRight">
                        <el-breadcrumb-item v-for="item in pathList" @click="pageClicked(item)"
                            :class="item.id == currentNode.id ? 'currentBread' : ''">{{ item.label }}
                        </el-breadcrumb-item>
                    </el-breadcrumb>

                </el-header>
                <el-main>





                    <MarkdownEditor v-if="editMode" v-model="pageDetail.content" />

                    <div v-if="!editMode" class="markdown-body">
                        <Markdown :source="pageDetail.content || ''" />
                    </div>

                    <el-tree v-if="showIndex" allow-drop="true" allow-drag="true" :highlight-current="true"
                        ref="treeRef" :data="data" draggable default-expand-all node-key="id"
                        :expand-on-click-node=false @node-drag-start="handleDragStart"
                        @node-drag-enter="handleDragEnter" @node-drag-leave="handleDragLeave"
                        @node-drag-over="handleDragOver" @node-drag-end="handleDragEnd" @node-drop="handleDrop"
                        @node-click="handleNodeClick">
                        <template #default="{ node, data }">
                            <span class="custom-tree-node">
                                <span>{{ node.label }}</span>
                                <span>

                                    <a id="btnDel" style="margin-left: 8px">
                                        <el-icon>
                                            <Delete />
                                        </el-icon>
                                    </a>
                                </span>
                            </span>
                        </template>

                    </el-tree>



                    <el-drawer v-model="drawer2" :direction="direction" style="top: 50px">
                        <template #title>
                            <h3>目录</h3>
                        </template>


                        <template #default>



                        </template>
                        <template #footer>
                            <div style="flex: auto">
                                <el-button @click="cancelClick">cancel</el-button>
                                <el-button type="primary" @click="confirmClick">confirm</el-button>
                            </div>
                        </template>
                    </el-drawer>

                </el-main>
            </el-container>
        </el-container>
    </div>


</template>

<script lang="ts" setup>
import api from '../../api'
import { ref, reactive } from 'vue'
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

let id = router.currentRoute.value.query.id

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

let pathList = ref<TreeNode[]>([{ 'id': '0', 'label': 'root' }])

let kbMetaData = ref<KbMetaData>()

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

let currentNode = ref({ 'id': '0', 'label': 'root' })

const pageClicked = (node) => {
    let nodeId = node.id

    refreshPage(nodeId)

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

}

const addPage = () => {
    let data = {
        knowledgeBaseId: id,
        parentId: currentNode.value.id,
        label: newPageName.value
    }
    api.post('/page/add', data).then(response => {
        if (response.data.code === "200") {

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

const refreshPage = (nodeId) => {
    api.get('/page/detail?id=' + nodeId).then(response => {
        if (response.data.code === "200") {
            pageDetail.value = response.data.data
        }
    });

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

}

const treeRef = ref<InstanceType<typeof ElTree>>()

const setCheckedNodes = (node) => {

    let id = node.id === "0" ? null : node.id
    treeRef.value!.setCurrentKey(
        id,
        true
    )
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
        drawer2.value = true
    }
    api.get('/page/list?knowledgeBaseId=' + kbMetaData.value?.id).then(response => {
        if (response.data.code === "200") {
            data.value = response.data.data
        }
    });
}

const savePage = () => {
    api.post('/page/update', pageDetail.value).then(response => {
        if (response.data.code === "200") {
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

#btnAdd:hover {
    color: green;
}

#btnDel:hover {
    color: red;
}

#btnAdd {
    color: yellowgreen;
}

#btnDel {
    color: rebeccapurple;
}

#btnAddPage {
    font-size: 15px;
}

#btnAddPage:hover {
    color: green;
}

.el-breadcrumb {
    margin: 20px auto;
    background-color: #DCDFE6;
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
    background-color: #CCCCCC;
    cursor: pointer !important;
}

.el-breadcrumb__item:first-child {

    line-height: 40px;
}

.currentBread {
    color: #EFEFEF !important;
    background-color: #CCCCCC;
}

.markdown-body {
    padding: 30px;
    border-radius: 5px;
}

.navBar {
    height: 140px !important;
    background-color: #FFFFFF;
    padding: 20px;
    z-index: 999;
    margin: 20px;
    margin-right: 35px;
    border-radius: 5px;
    padding-bottom: 0 !important;
}

.el-main {
    flex-basis: 0 !important;
    padding-top: 0 !important;
}
</style>