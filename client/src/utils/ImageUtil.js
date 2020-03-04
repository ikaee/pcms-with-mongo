import React from 'react';

export const  imageGenartion = record => {
      const MIME = "data:image/jpeg;base64,";
      const image = <img src={MIME + record.image} alt="image" style={{"height": "40px", "width": "40px"}}/>;
      const newRecord = Object.assign({}, record, {"image": image})
      return newRecord
}
