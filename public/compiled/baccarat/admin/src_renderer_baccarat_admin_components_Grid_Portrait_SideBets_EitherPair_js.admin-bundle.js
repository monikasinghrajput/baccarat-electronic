"use strict";
/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunktykhe_electron_apps_baccarat"] = self["webpackChunktykhe_electron_apps_baccarat"] || []).push([["src_renderer_baccarat_admin_components_Grid_Portrait_SideBets_EitherPair_js"],{

/***/ "./src/renderer/baccarat/admin/components/Grid/Portrait/SideBets/EitherPair.js":
/*!*************************************************************************************!*\
  !*** ./src/renderer/baccarat/admin/components/Grid/Portrait/SideBets/EitherPair.js ***!
  \*************************************************************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export */ __webpack_require__.d(__webpack_exports__, {\n/* harmony export */   \"default\": () => (__WEBPACK_DEFAULT_EXPORT__)\n/* harmony export */ });\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! react */ \"./node_modules/react/index.js\");\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(react__WEBPACK_IMPORTED_MODULE_0__);\n/* harmony import */ var _emotion_css__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @emotion/css */ \"./node_modules/@emotion/css/dist/emotion-css.esm.js\");\n/* harmony import */ var prop_types__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! prop-types */ \"./node_modules/prop-types/index.js\");\n/* harmony import */ var prop_types__WEBPACK_IMPORTED_MODULE_8___default = /*#__PURE__*/__webpack_require__.n(prop_types__WEBPACK_IMPORTED_MODULE_8__);\n/* harmony import */ var _style__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../../style */ \"./src/renderer/baccarat/admin/components/Grid/style.js\");\n/* harmony import */ var _constants__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../../../../constants */ \"./src/renderer/baccarat/admin/constants/index.js\");\n/* harmony import */ var _components_BetLabel__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../../components/BetLabel */ \"./src/renderer/baccarat/admin/components/Grid/components/BetLabel/index.js\");\n/* harmony import */ var _components_Chip__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../../components/Chip */ \"./src/renderer/baccarat/admin/components/Grid/components/Chip/index.js\");\n/* harmony import */ var _uiConfig__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ../../../../uiConfig */ \"./src/renderer/baccarat/admin/uiConfig.js\");\n/* harmony import */ var _emotion_react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @emotion/react/jsx-runtime */ \"./node_modules/@emotion/react/jsx-runtime/dist/emotion-react-jsx-runtime.browser.esm.js\");\n/* eslint-disable max-len */\n\n\n\n\n\n\n\n\n\n\nconst {\n  PAYOUTS\n} = _uiConfig__WEBPACK_IMPORTED_MODULE_6__.UI_CONFIG;\nfunction getProps(disabled) {\n  if (disabled) {\n    return {\n      g: {\n        transform: 'translate(185.176083, 0)'\n      },\n      path: {\n        d: 'M0,0 L173.7904137,0 C177.2086917,-8.11624501e-16 179.7904137,3.581722 179.7904137,8 L179.7904137,22 C179.7904137,26.418278 177.2086917,30 173.7904137,30 L0,30 L0,0 Z'\n      },\n      label: {\n        width: 181,\n        height: 30\n      },\n      chip: {\n        x: 76\n      }\n    };\n  }\n  return {\n    g: {\n      transform: 'translate(276.176083, 0)'\n    },\n    path: {\n      d: 'M0,0 L80.7904137,0 C85.2086917,-8.11624501e-16 88.7904137,3.581722 88.7904137,8 L88.7904137,22 C88.7904137,26.418278 85.2086917,30 80.7904137,30 L0,30 L0,0 Z'\n    },\n    label: {\n      width: 92,\n      height: 30\n    },\n    chip: {\n      x: 31\n    }\n  };\n}\n/*\nWe need to add unused props to avoid following react warning:\nindex.js:1 Warning: React does not recognize the `disabledPlayerPairAndBankerPair` prop on a DOM element.\nIf you intentionally want it to appear in the DOM as a custom attribute, spell it as lowercase `disabledplayerpairandbankerpair` instead.\nIf you accidentally passed it from a parent component, remove it from the DOM element.\n* */\nconst EitherPair = ({\n  locales,\n  placeBet,\n  disabledSmallAndBig,\n  disabledPerfectPairAndEitherPair,\n  disabledPlayerPairAndBankerPair,\n  ...rest\n}) => {\n  const {\n    g,\n    path,\n    label,\n    chip\n  } = (0,react__WEBPACK_IMPORTED_MODULE_0__.useMemo)(() => getProps(disabledSmallAndBig), [disabledSmallAndBig]);\n  return (0,_emotion_react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsxs)(\"g\", {\n    id: \"bet-either-pair\",\n    onClick: () => !disabledPerfectPairAndEitherPair && placeBet(_constants__WEBPACK_IMPORTED_MODULE_3__.BET_TYPES.EITHER_PAIR),\n    ...rest,\n    ...g,\n    children: [(0,_emotion_react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsx)(\"path\", {\n      className: \"highlight\",\n      stroke: _constants__WEBPACK_IMPORTED_MODULE_3__.BOARD_COLORS.SIDEBETS,\n      fillOpacity: \"0.65\",\n      fill: _constants__WEBPACK_IMPORTED_MODULE_3__.BOARD_COLORS.SIDEBETS,\n      ...path\n    }), (0,_emotion_react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsxs)(_components_BetLabel__WEBPACK_IMPORTED_MODULE_4__[\"default\"], {\n      className: (0,_emotion_css__WEBPACK_IMPORTED_MODULE_1__.cx)(_style__WEBPACK_IMPORTED_MODULE_2__.sideBetsLabelClassName, _style__WEBPACK_IMPORTED_MODULE_2__.portraitSideBets),\n      ...label,\n      children: [locales.eitherPair, (0,_emotion_react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsx)(\"br\", {}), PAYOUTS[_constants__WEBPACK_IMPORTED_MODULE_3__.BET_TYPES.EITHER_PAIR], \":1\"]\n    }), (0,_emotion_react_jsx_runtime__WEBPACK_IMPORTED_MODULE_7__.jsx)(_components_Chip__WEBPACK_IMPORTED_MODULE_5__[\"default\"], {\n      betType: _constants__WEBPACK_IMPORTED_MODULE_3__.BET_TYPES.EITHER_PAIR,\n      ...chip\n    })]\n  });\n};\nEitherPair.propTypes = {\n  label: prop_types__WEBPACK_IMPORTED_MODULE_8__.string,\n  placeBet: prop_types__WEBPACK_IMPORTED_MODULE_8__.func,\n  locales: prop_types__WEBPACK_IMPORTED_MODULE_8__.object,\n  disabledSmallAndBig: prop_types__WEBPACK_IMPORTED_MODULE_8__.bool,\n  disabledPlayerPairAndBankerPair: prop_types__WEBPACK_IMPORTED_MODULE_8__.bool,\n  disabledPerfectPairAndEitherPair: prop_types__WEBPACK_IMPORTED_MODULE_8__.bool\n};\n/* harmony default export */ const __WEBPACK_DEFAULT_EXPORT__ = (EitherPair);\n\n//# sourceURL=webpack://tykhe-electron-apps-baccarat/./src/renderer/baccarat/admin/components/Grid/Portrait/SideBets/EitherPair.js?");

/***/ })

}]);