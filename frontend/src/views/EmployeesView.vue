<template>
  <div>
    <h1>Служители</h1>
    <div class="card">
      <div class="actions" style="margin-bottom: 1rem;">
        <router-link to="/employees/new" class="btn btn-primary">Добави служител</router-link>
        <label style="margin-left: auto; display: flex; align-items: center; gap: 0.5rem;">
          <input type="checkbox" v-model="activeOnly" @change="load" />
          Само активни
        </label>
      </div>
      <p v-if="loading">Зареждане...</p>
      <p v-else-if="error" class="error">{{ error }}</p>
      <table v-else-if="list.length">
        <thead>
          <tr>
            <th>Име</th>
            <th>Имейл</th>
            <th>Роля</th>
            <th>Часове/седмица</th>
            <th>Активен</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="e in list" :key="e.id">
            <td>{{ e.firstName }} {{ e.lastName }}</td>
            <td>{{ e.email }}</td>
            <td>{{ e.role }}</td>
            <td>{{ e.contractHoursPerWeek }}</td>
            <td>{{ e.active ? 'Да' : 'Не' }}</td>
            <td>
              <router-link :to="`/employees/${e.id}`" class="btn btn-secondary">Редактирай</router-link>
              <button type="button" class="btn btn-danger" @click="remove(e.id)">Изтрий</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-else>Няма служители.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { employees as api } from '../api/client'

const list = ref([])
const loading = ref(true)
const error = ref('')
const activeOnly = ref(false)

async function load() {
  loading.value = true
  error.value = ''
  try {
    const res = await api.list(activeOnly.value)
    list.value = res.data
  } catch (e) {
    error.value = e.response?.data?.error || e.message || 'Грешка при зареждане'
  } finally {
    loading.value = false
  }
}

async function remove(id) {
  if (!confirm('Изтриване на служител?')) return
  try {
    await api.delete(id)
    await load()
  } catch (e) {
    error.value = e.response?.data?.error || e.message || 'Грешка при изтриване'
  }
}

onMounted(load)
</script>
