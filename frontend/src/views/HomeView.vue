<template>
  <div class="card">
    <h1>Workforce Management</h1>
    <p>Добре дошли в системата за управление на смени и работна сила.</p>
    <ul>
      <li><router-link to="/employees">Служители</router-link> – преглед и управление на служители</li>
      <li><router-link to="/shifts">Смени</router-link> – планиране и преглед на смени</li>
      <li><router-link to="/vacation-requests">Отпуски</router-link> – заявки за отпуск</li>
    </ul>
  </div>
</template>

<script setup>
</script>
