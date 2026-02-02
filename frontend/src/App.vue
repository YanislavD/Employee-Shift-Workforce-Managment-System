<template>
  <div class="app">
    <nav class="nav">
      <router-link to="/">Начало</router-link>
      <router-link to="/employees">Служители</router-link>
      <router-link to="/shifts">Смени</router-link>
      <router-link to="/vacation-requests">Отпуски</router-link>
      <label class="nav-user">
        Влизам като:
        <select v-model="currentUserId" @change="onUserChange">
          <option value="">— избери —</option>
          <option v-for="e in employees" :key="e.id" :value="e.id">
            {{ e.firstName }} {{ e.lastName }} ({{ e.role }})
          </option>
        </select>
      </label>
    </nav>
    <main class="main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, provide, onMounted } from 'vue'
import { employees as api } from './api/client'

const currentUserId = ref(localStorage.getItem('demoUser') || '')
const currentUser = ref(null)
const employees = ref([])

provide('currentUser', currentUser)
provide('employees', employees)

async function loadEmployees() {
  try {
    const res = await api.list(false)
    employees.value = res.data
    if (currentUserId.value) {
      const e = res.data.find(x => String(x.id) === String(currentUserId.value))
      currentUser.value = e || null
    }
  } catch (_) {}
}

function onUserChange() {
  if (currentUserId.value) {
    localStorage.setItem('demoUser', currentUserId.value)
    currentUser.value = employees.value.find(x => String(x.id) === String(currentUserId.value)) || null
  } else {
    localStorage.removeItem('demoUser')
    currentUser.value = null
  }
}

onMounted(async () => {
  await loadEmployees()
  onUserChange()
})
</script>

<style>
* { box-sizing: border-box; }
body { margin: 0; font-family: system-ui, sans-serif; background: #f5f5f5; }
.app { min-height: 100vh; display: flex; flex-direction: column; }
.nav {
  background: #1a1a2e;
  padding: 1rem 1.5rem;
  display: flex;
  gap: 1.5rem;
}
.nav a {
  color: #eee;
  text-decoration: none;
  font-weight: 500;
}
.nav a:hover, .nav a.router-link-active { color: #4fc3f7; }
.nav-user { margin-left: auto; display: flex; align-items: center; gap: 0.5rem; color: #ccc; }
.nav-user select { padding: 0.25rem 0.5rem; border-radius: 4px; background: #2d2d44; color: #eee; border: 1px solid #444; }
.main { flex: 1; padding: 1.5rem; max-width: 1200px; margin: 0 auto; width: 100%; }
h1 { margin-top: 0; color: #1a1a2e; }
.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  padding: 1.25rem;
  margin-bottom: 1rem;
}
table { width: 100%; border-collapse: collapse; }
th, td { text-align: left; padding: 0.75rem; border-bottom: 1px solid #eee; }
th { background: #f8f9fa; font-weight: 600; color: #333; }
.btn {
  display: inline-block;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 500;
}
.btn-primary { background: #1976d2; color: white; }
.btn-primary:hover { background: #1565c0; }
.btn-danger { background: #c62828; color: white; }
.btn-danger:hover { background: #b71c1c; }
.btn-secondary { background: #757575; color: white; }
.btn-secondary:hover { background: #616161; }
form label { display: block; margin-bottom: 0.25rem; font-weight: 500; color: #444; }
form input, form select, form textarea {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
}
form input:focus, form select:focus, form textarea:focus {
  outline: none;
  border-color: #1976d2;
}
.error { color: #c62828; margin-top: 0.5rem; font-size: 0.9rem; }
.actions { display: flex; gap: 0.5rem; flex-wrap: wrap; }
</style>
