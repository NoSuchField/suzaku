<template>
  <div class="login-form">
    <el-form :model="data">
      <h1 style="font-size: 25px; height: 120px;line-height: 120px; user-select: none;">Knowledgebase</h1>
      <el-form-item>
        <el-input v-model="data.username" placeholder="Username" />
      </el-form-item>
      <el-form-item>
        <el-input v-model="data.password" placeholder="Password" />
      </el-form-item>
      <div class="login-button" @click="onSubmit">Login</div>
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
        username: '',
        password: ''
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

<style scoped>
* {
  width: 100%;
  margin: 0;
  padding: 0;
}

.el-container {
  background-color: #010409;
}

.login-form {
  border: 1px solid #30363D;
  background-color: #0D1117;
  margin: 0 auto;
  margin-top: 30vh;
  width: 400px;
  text-align: center;

  padding: 80px;
  padding-top: 0;
  padding-bottom: 40px;
  border-radius: 10px;

}

.login-button {
  text-align: center;
  background-color: #21262D;
  line-height: 36px;
  border: 1px solid #363B42;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 30px;
}

.el-input {
  height: 36px;
}

.login-button:hover {
  background-color: #30363D;
  border: 1px solid #8B949E;
}
</style>