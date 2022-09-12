<template>
  <div style="position: fixed; width: 100%; height: 100%; top:0; left: 0; color: #000000; background-color: azure;">
    <el-form :model="data" label-width="120px">
      <el-form-item label="username">
        <el-input v-model="data.username" />
      </el-form-item>
      <el-form-item label="password">
        <el-input v-model="data.password" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">Login</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>

<script>

import api from '../api'

export default {
  name: 'Login',
  data: () => {
    return {
      data: {
        username: 'admin',
        password: 'F&kAx8#ybyZ8dQ'
      }
    }
  },
  methods: {
    onSubmit() {
      api.post('/system/login', this.data).then(response => {
        if (response.data.code === "200") {
          window.localStorage.setItem('token', response.data.data.token);
          this.$router.push('/');
        }
      });
    }
  }
}
</script>