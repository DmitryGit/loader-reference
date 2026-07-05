<template>
  <header class="rounded-4" style="background-color: crimson; height: 100px !important; margin: 10px"><div align="right" style="padding: 30px"><button type="button" class="btn btn-light"><strong>Профиль</strong></button></div></header>
  <div style="display: flex; width: 100%;">
  <div style="font-size: 24px; height: 70vh; background-color: lightgray; width: 200px; padding: 10px; margin: 10px" class="rounded-4">
<!--  <div style="font-size: 24px; float: left; height: 90vh; background-color: lightgray; width: 200px; padding: 10px; margin-top: 10px" class="rounded-4">-->
    <div style="margin-top: 50px;">Пользователи</div>
    <div style="margin-top: 50px;">Уведомления и напоминания</div>
    <div style="margin-top: 50px;">Настройки АИС ОГПА</div>
    <div style="margin-top: 50px; color: crimson">Справочник погрузчиков</div>
    <div style="margin-top: 50px;">Резервное копирование и восстановление</div>
    <div style="margin-top: 50px;">Справочники</div>
  </div>
  <div id="app" class="rounded-4 w-auto" style="margin: 10px; background-color: lightgray; flex-grow: 1; padding: 10px" >
<!--  <div id="app" class="container rounded-4 w-auto" style="margin: 10px; background-color: lightgray; float: left" >-->
    <h1><strong>Справочник погрузчиков</strong></h1>
    <div class="row">
      <div class="col-md-7">
        <loader-table
          @select-loader="handleSelectLoader"
          :incident-count="incidentCount"
          ref="loaderTable"
        />
      </div>
      <div class="col-md-5">
        <incident-list
          :loader-id="selectedLoaderId"
          @incident-count="handleIncidentCount"
        />
      </div>
    </div>
  </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import LoaderTable from './components/LoaderTable.vue';
import IncidentList from './components/IncidentList.vue';

const selectedLoaderId = ref(null);
const incidentCount = ref(0);

function handleSelectLoader(id) {
  selectedLoaderId.value = id;
  console.log('selectedLoaderId.value', selectedLoaderId.value)
}

function handleIncidentCount(count) {
  incidentCount.value = count;
  console.log('incidentCount.value', incidentCount.value)
}
</script>

<style>
/* Дополнительные стили при необходимости */
</style>