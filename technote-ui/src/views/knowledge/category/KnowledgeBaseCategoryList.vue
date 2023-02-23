<template>
    <el-row class="mb-4">
        <el-button type="primary" @click="newCategory">
            <el-icon>
                <Plus />
            </el-icon>新建分类
        </el-button>
    </el-row>

    <el-table :data="tableData" style="width: 100%">

        <el-table-column label="图标" width="70px">

            <template #default="scope">
                <div style="display: flex; align-items: center">

                    <img :src="scope.row.icon" style="width: 40px; height: 40px; border-radius: 5px;" />

                </div>
            </template>
        </el-table-column>
        <el-table-column label="分类 id" prop="id" />
        <el-table-column label="分类名称" prop="name" />
        <el-table-column label="创建时间" prop="createdAt" :formatter="fmtdate" />
        <el-table-column align="right">
            <template #header>
                <el-input v-model="search" size="small" placeholder="Type to search" />
            </template>
            <template #default="scope">
                <el-button size="small" @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
                <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">Delete</el-button>
            </template>
        </el-table-column>
    </el-table>


    <el-dialog v-model="dialogFormVisible" title="新建知识库分类">
        <el-form :model="form">
            <el-form-item label="分类名称" label-width="120px">
                <el-input v-model="form.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="分类图标" label-width="120px">
                <el-upload class="avatar-uploader" action="https://img.omgxy.com/api/upload" :data=tokenData
                    :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                    <img v-if="form.icon" :src="form.icon" class="avatar" />
                    <el-icon v-else class="avatar-uploader-icon">
                        <Plus />
                    </el-icon>
                </el-upload>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogFormVisible = false">Cancel</el-button>
                <el-button type="primary" @click="submitAddCategory">Confirm</el-button>
            </span>
        </template>
    </el-dialog>

    <el-dialog v-model="confirmDeleteDialogVisiable" title="删除分类" width="20%">
        <span>确定删除该分类吗?</span>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="confirmDeleteDialogVisiable = false">取消</el-button>
                <el-button type="primary" @click="doDelete">确定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup>

import api from '../../../api'

import { computed, ref } from 'vue'
import type { UploadProps } from 'element-plus'
import { ElMessage } from 'element-plus'
import moment from 'moment'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()

interface User {
    id: string
    icon: string
    name: string

}

const search = ref('')

const refresh = () => {
    router.go(0)
}

const fmtdate = (row, column, cellValue, index) => {
    return moment.unix(cellValue / 1000).format('YYYY-MM-DD HH:mm:ss')
}

const tokenData = {
    token: '88769b4b0be6016d1f12045a2ca85e5a'
}

const handleAvatarSuccess: UploadProps['onSuccess'] = (
    response,
    uploadFile
) => {
    form.value.icon = response.url
}


const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
    if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
        ElMessage.error('Avatar picture must be JPG or PNG format!')
        return false
    } else if (rawFile.size / 1024 / 1024 > 5) {
        ElMessage.error('Avatar picture size can not exceed 5MB!')
        return false
    }
    return true
}

const submitAddCategory = () => {
    api.post('/category/' + (form.value.id ? 'update' : 'add'), form.value).then(response => {
        if (response.data.code === "200") {
            dialogFormVisible.value = false
            refresh()
        }
    });
}
let form = ref({
    id: '',
    name: '',
    region: '',
    icon: ''
})


const handleEdit = (index, row) => {
    dialogFormVisible.value = true
    form.value = row
}
let dialogFormVisible = ref(false)
let confirmDeleteDialogVisiable = ref(false)
const newCategory = () => {
    form.value = {
        id: '',
        name: '',
        region: '',
        icon: ''
    }
    dialogFormVisible.value = true
}

const handleDelete = (index, row) => {
    form.value = row
    confirmDeleteDialogVisiable.value = true
}

const doDelete = () => {
    api.post('/category/delete?id=' + form.value.id, {}).then(response => {
        if (response.data.code === "200") {
            confirmDeleteDialogVisiable.value = false
            refresh()
        }
    });
}

let tableData = ref([])


api.post('/category/list', {}).then(response => {
    if (response.data.code === "200") {
        tableData.value = response.data.data;
    }
});
</script>

<style>
.avatar {
    width: 180px;
    height: 180px;
}
</style>