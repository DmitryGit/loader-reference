<template>
  <header class="rounded-4" style="background-color: crimson; height: 100px !important; margin: 10px"><div align="right" style="padding: 30px"><button type="button" class="btn btn-light"><strong>Профиль</strong></button></div></header>
  <div style="display: flex; width: 100%; height: 80vh;">
  <div style="font-size: 24px; background-color: lightgray; width: 200px; padding: 10px; margin: 10px; height: clamp(650px, 80vh, calc(100vh - 50px));" class="rounded-4">
    <div style="margin-top: 30px;">Пользователи</div>
    <div style="margin-top:30px;">Уведомления и напоминания</div>
    <div style="margin-top: 30px;">Настройки АИС ОГПА</div>
    <div style="margin-top: 30px; color: crimson">Справочник погрузчиков</div>
    <div style="margin-top: 30px;">Резервное копирование и восстановление</div>
    <div style="margin-top: 30px;margin-bottom: 30px;">Справочники</div>
  </div>
  <div id="app" class="rounded-4 w-auto" style="margin: 10px; background-color: lightgray; flex-grow: 1; padding: 10px; height: clamp(650px, 80vh, calc(100vh - 50px));" >
    <h1><strong>Справочник погрузчиков</strong></h1>
    <div class="row">
      <div class="col-md-7">
        <loader-table
          @select-loader="handleSelectLoader"
          @select-loader-number="handleSelectLoaderNumber"
          :incident-count="incidentCount"
          ref="loaderTable"
        />
      </div>
      <div class="col-md-5">
        <incident-list
          :loader-id="selectedLoaderId"
          :loader-number="selectedLoaderNumber"
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
const selectedLoaderNumber = ref(null);
const incidentCount = ref(0);

function handleSelectLoader(id) {
  selectedLoaderId.value = id;
}

function handleSelectLoaderNumber(number) {
  selectedLoaderNumber.value = number;
}

function handleIncidentCount(count) {
  incidentCount.value = count;
}
</script>

<style>
/* Дополнительные стили при необходимости */
</style>