<template>
  <div>
    <h1>Смени</h1>
    <div class="card">
      <div style="margin-bottom: 1rem;">
        <label>Филтър по дата</label>
        <input type="date" v-model="filterDate" @change="load" style="max-width: 200px;" />
        <button type="button" class="btn btn-secondary" @click="filterDate = ''; load()" style="margin-left: 0.5rem;">Изчисти</button>
      </div>
      <p v-if="loading">Зареждане...</p>
      <p v-else-if="error" class="error">{{ error }}</p>
      <table v-else-if="list.length">
        <thead>
          <tr>
            <th>Дата</th>
            <th>Начало</th>
            <th>Край</th>
            <th>Роля</th>
            <th>Статус</th>
            <th>Служител</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.shiftDate }}</td>
            <td>{{ s.startTime }}</td>
            <td>{{ s.endTime }}</td>
            <td>{{ s.requiredRole }}</td>
            <td>{{ s.status }}</td>
            <td>{{ s.employeeName || '–' }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>Няма смени.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { shifts as api } from '../api/client'

const list = ref([])
const loading = ref(true)
const error = ref('')
const filterDate = ref('')

async function load() {
  loading.value = true
  error.value = ''
  try {
    const params = filterDate.value ? { date: filterDate.value } : {}
    const res = await api.list(params)
    list.value = res.data
  } catch (e) {
    error.value = e.response?.data?.error || e.message || 'Грешка при зареждане'
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
