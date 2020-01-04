<template>
  <div class="md:w-56 px-2 py-3 rounded overflow-hidden border shadow-md my-2" style="background-color: #ffa866">
    <div class="font-bold text-xl smallcaps mb-2">test results</div>
    Title: {{ title }}
    <p class="text-black font-bold text-base mb-2">
      <code>&gt;= 2 </code>
      <span class="card-title">code smaller</span>
      cards<br/>As Predicted
    </p>
    <p class="text-gray-800 text-sm px-1">
      The tests ran and matched your prediction.
    </p>
    <p class="text-black font-bold text-base my-2">
      <code>&lt;= 1 </code>
      <span class="card-title">code smaller</span>
      cards<br/>Unexpected
    </p>
    <p class="text-gray-800 text-sm px-1">
      The tests did <strong>not</strong> match your prediction. Try again.
    </p>
  </div>

</template>

<script>
  export default {
    name: 'test-results-card',
    props: {
      title: {
        type: String,
        required: true
      }
    }

  }
</script>
