import { defineStore } from 'pinia';

interface Translations {
  [key: string]: {
    [key: string]: string;
  };
}

export const useutf8Store = defineStore('utf8', {
  state: () => ({
    currentLanguage: 'es',
    translations: {
      en: {
        'login.title': 'Sign in to your account',
        'login.email': 'Email address',
        'login.password': 'Password',
        'login.remember': 'Remember me',
        'login.forgot': 'Forgot your password?',
        'login.submit': 'Sign in',
        'language': 'Language',
        'language.en': 'English',
        'language.es': 'Spanish',
        'language.cat': 'Catalan',
        'language.select': 'Select your language',
      },
      es: {
        'login.title': 'Iniciar sesión',
        'login.email': 'Correo electrónico',
        'login.password': 'Contraseña',
        'login.remember': 'Recordarme',
        'login.forgot': '¿Olvidaste tu contraseña?',
        'login.submit': 'Entrar',
        'language': 'Idioma',
        'language.en': 'Inglés',
        'language.es': 'Español',
        'language.cat': 'Catalán',
        'language.select': 'Selecciona tu idioma',
      },
      cat: {
        'login.title': 'Iniciar sessió',
        'login.email': 'Correu electrònic',
        'login.password': 'Contrasenya',
        'login.remember': 'Recorda\'m',
        'login.forgot': 'Has oblidat la teva contrasenya?',
        'login.submit': 'Entrar',
        'language': 'Idioma',
        'language.en': 'Anglès',
        'language.es': 'Espanyol',
        'language.cat': 'Català',
        'language.select': 'Selecciona el teu idioma',
      },
    } as Translations
  }),
  actions: {
    setLanguage(lang: string) {
      this.currentLanguage = lang;
    },
    t(key: string): string {
      return this.translations[this.currentLanguage][key] || key;
    }
  }
});
