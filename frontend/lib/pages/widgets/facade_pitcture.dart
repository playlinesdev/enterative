import 'dart:typed_data';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

final TextStyle labelStyle = GoogleFonts.roboto(
  fontWeight: FontWeight.bold,
  color: Color(0xff555555),
  fontSize: 15,
);

class FacadePicture extends StatefulWidget {
  @override
  _FacadePictureState createState() => _FacadePictureState();
}

class _FacadePictureState extends State<FacadePicture> {
  Uint8List fileBytes = Uint8List(0);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('Foto da fachada (loja física):', style: labelStyle),
        SizedBox(height: 10),
        if (fileBytes.length > 0) Image.memory(fileBytes),
        if (fileBytes.length > 0) SizedBox(height: 10),
        ElevatedButton(
            onPressed: () {
              uploadFile();
            },
            child: Text('Upload')),
      ],
    );
  }

  void uploadFile() async {
    var result = await FilePicker.platform.pickFiles(allowMultiple: false, type: FileType.image);
    setState(() {
      fileBytes = result!.files.single.bytes!;
    });
  }
}
