import { defineStore } from 'pinia';

export const useMessageStore = defineStore('message', {
  state: () => ({
    message: 'Updated from Pinia!'
  }),
  actions: {
    setMessage(newMessage: string) {
      this.message = newMessage; // ¡No necesitas "mutations" en Pinia!
    }
  }
});
