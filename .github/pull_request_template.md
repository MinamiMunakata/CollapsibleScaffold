<!-- I want to review in Japanese. -->

## **Overview**

## **Description**

<!-- for GitHub Copilot review rule -->
## レビューに関して

このPull Request（PR）をレビューする際には、以下の **Shields.ioバッジ** を **prefix（接頭辞）** としてコメント冒頭に貼り付けてください。  
レビューコメントの **意図や重要度を明確に伝える** ためのルールです。

### 利用可能なレビューprefixバッジ

| Prefix  | バッジ | 意味 / 用途例 |
|---------|--------|----------------|
| `[must]` | ![must](https://img.shields.io/badge/must-red) | **必ず修正が必要**：バグ・仕様逸脱・セキュリティ問題など。 |
| `[imo]` | ![imo](https://img.shields.io/badge/imo-blue) | *In my opinion*：主観的提案。任意対応。 |
| `[niche]` | ![niche](https://img.shields.io/badge/niche-lightgrey) | 特定条件でのみ発生する懸念。優先度は低め。 |
| `[ask]` | ![ask](https://img.shields.io/badge/ask-yellow) | 質問・仕様確認などのコメント。 |
| `[fyi]` | ![fyi](https://img.shields.io/badge/fyi-success) | 情報共有。修正は不要。参考情報など。 |

### コメント記述例

```md
![Static Badge](https://img.shields.io/badge/must-red) この処理はnullチェックが漏れていてクラッシュの原因になります。修正をお願いします。

![Static Badge](https://img.shields.io/badge/imo-blue) この変数名はより明確なものにできると思います。

![Static Badge](https://img.shields.io/badge/niche-lightgrey) 再現は難しそうですが、例外が出る可能性があります。

![Static Badge](https://img.shields.io/badge/ask-yellow) この処理はバックエンドの変更を前提としていますか？

![Static Badge](https://img.shields.io/badge/fyi-success) このライブラリは次期バージョンでDeprecated予定とのことです。

```

<!-- for GitHub Copilot review rule -->

<!-- I want to review in Japanese. -->
