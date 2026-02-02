<template>
  <div>
    <h1>{{ id ? 'Редактиране на служител' : 'Нов служител' }}</h1>
    <div class="card">
      <p v-if="error" class="error">{{ error }}</p>
      <form @submit.prevent="submit">
        <label>Име</label>
        <input v-model="form.firstName" required />
        <label>Фамилия</label>
        <input v-model="form.lastName" required />
        <label>Имейл</label>
        <input v-model="form.email" type="email" required />
        <label>Роля</label>
        <select v-model="form.role" required>
          <option value="EMPLOYEE">EMPLOYEE</option>
          <option value="MANAGER">MANAGER</option>
          <option value="ADMIN">ADMIN</option>
        </select>
        <label>Часове по договор (седмично)</label>
        <input v-model.number="form.contractHoursPerWeek" type="number" min="1" max="168" required />
        <label>
          <input type="checkbox" v-model="form.active" />
          Активен
        </label>
        <div class="actions" style="margin-top: 1rem;">
          <button type="submit" class="btn btn-primary">Запис</button>
          <router-link to="/employees" class="btn btn-secondary">Отказ</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { employees as api } from '../api/client'

const route = useRoute()
const router = useRouter()
const id = computed(() => route.params.id ? Number(route.params.id) : null)

const form = reactive({
  firstName: '',
  lastName: '',
  email: '',
  role: 'EMPLOYEE',
  contractHoursPerWeek: 40,
  active: true,
})
const error = ref('')
const loading = ref(false)

async function load() {
  if (!id.value) return
  loading.value = true
  error.value = ''
  try {
    const res = await api.get(id.value)
    Object.assign(form, {
      firstName: res.data.firstName,
      lastName: res.data.lastName,
      email: res.data.email,
      role: res.data.role,
      contractHoursPerWeek: res.data.contractHoursPerWeek,
      active: res.data.active,
    })
  } catch (e) {
    error.value = e.response?.data?.error || e.message || 'Грешка при зареждане'
  } finally {
    loading.value = false
  }
}

async function submit() {
  error.value = ''
  try {
    if (id.value) {
      await api.update(id.value, form)
    } else {
      await api.create(form)
    }
    router.push('/employees')
  } catch (e) {
    error.value = e.response?.data?.error || (e.response?.data?.details && JSON.stringify(e.response.data.details)) || e.message || 'Грешка при запис'
  }
}

onMounted(load)
</script>
