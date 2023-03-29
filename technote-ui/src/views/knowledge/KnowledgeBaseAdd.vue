<template>
  <h1>Add</h1>

  <el-form :model="form" label-width="120px">
    <el-form-item label="知识库名称">
      <el-input v-model="form.name" />
    </el-form-item>

    <el-form-item label="知识库描述">
      <el-input v-model="form.description" type="textarea" />
    </el-form-item>

    <el-form-item label="封面图片">
      <el-upload class="avatar-uploader" action="https://img.omgxy.com/api/upload" :data=tokenData :show-file-list="false"
        :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
        <img v-if="form.coverImage" :src="form.coverImage" class="avatar" />
        <el-icon v-else class="avatar-uploader-icon">
          <Plus />
        </el-icon>
      </el-upload>
    </el-form-item>

    <el-form-item>
      <el-select style="width: 100%" v-model="form.categoryIdList" multiple size="large" placeholder="Select">
        <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id">
          <span>{{ item.name }}</span>
        </el-option>
      </el-select>
    </el-form-item>


    <el-form-item>
      <el-button type="primary" @click="onSubmit">Create</el-button>
      <el-button @click="onCancel">Cancel</el-button>
    </el-form-item>
  </el-form>
</template>


<script lang="ts" setup>
import api from '../../api'
import { ref, reactive, } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

import type { UploadProps } from 'element-plus'

const router = useRouter()

let id = router.currentRoute.value.query.id

let category = router.currentRoute.value.query.category

let form = ref({
  name: '',
  description: '',
  coverImage: '',
  categoryIdList: (category && category !== '0') ? ref([category]) : ref([])
})

const onSubmit = () => {
  api.post("/book/" + (id ? "update" : "add"), form.value).then(response => {
    if (response.data.code === "200") {
      router.replace("/")
    }
  });
}

if (id) {
  api.get('/book/detail?id=' + id).then(response => {
    if (response.data.code === "200") {
      form.value = response.data.data
    }
  });
}

const coverImage = ref('')

interface Category {
  id: string
  icon: string
  name: string
}

let categoryList = ref<Category[]>([])

api.post('/category/list', {}).then(response => {
  if (response.data.code === "200") {
    categoryList.value = response.data.data;
  }
});

const tokenData = {
  token: '88769b4b0be6016d1f12045a2ca85e5a'
}

const onCancel = () => {
  router.replace("/")
}

const handleAvatarSuccess: UploadProps['onSuccess'] = (
  response,
  uploadFile
) => {
  form.value.coverImage = response.url
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
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>