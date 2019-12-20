import Vue from "vue";
import VueRouter from "vue-router";
import Game from "@/components/Game.vue";
import Connect from "../views/Connect.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    component: Connect
  },
  {
    path: "/game/player/:playerId",
    component: Game,
    props: true
  },
  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue")
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
